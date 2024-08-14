package com.example.taskomer.controllers;

import com.example.taskomer.DTO.TaskStateDTO;
import com.example.taskomer.exceptions.BadRequestException;
import com.example.taskomer.exceptions.NotFoundException;
import com.example.taskomer.model.Project;
import com.example.taskomer.model.TaskState;
import com.example.taskomer.repositories.ProjectRepo;
import com.example.taskomer.repositories.TaskStateRepo;
import com.example.taskomer.util.mappers.TaskStateMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class TaskStateController {
  private static final String CREATE_TASK_STATE = "/api/states/{project_id}";
  private static final String UPDATE_TASK_STATE = "/api/states/{id}";
  private static final String DELETE_TASK_STATE = "/api/states/{id}";
  private static final String SHOW_TASK_STATE = "/api/states";
  private static final String CHANGE_STATE_POSITION = "/api/states/{project_id}/position";
  private final TaskStateRepo stateRepo;
  private final ProjectRepo projectRepo;
  private final TaskStateMapper taskStateMapper;

  @GetMapping(SHOW_TASK_STATE)
  @Transactional
  public List<TaskStateDTO> getAllTaskStates(
          @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

    optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

    Stream<TaskState> states = optionalPrefixName
            .map(stateRepo::streamAllByStateNameIgnoreCaseStartingWith)
            .orElseGet(stateRepo::streamAllBy);

    return states
            .map(taskStateMapper::toTaskStateDTO)
            .collect(Collectors.toList());
  }

  @DeleteMapping(DELETE_TASK_STATE)
  @Transactional
  public Boolean deleteTaskState(@PathVariable Long id) {

    TaskState state = stateRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("Task state with id " + id + " not found"));

    Optional<Long> leftTaskStateId = Optional.ofNullable(state.getLeftTaskStateId());
    Optional<Long> rightTaskStateId = Optional.ofNullable(state.getRightTaskStateId());

    TaskState leftTaskState = leftTaskStateId.flatMap(stateRepo::findById).orElse(null);
    TaskState rightTaskState = rightTaskStateId.flatMap(stateRepo::findById).orElse(null);

    if (leftTaskState != null && rightTaskState != null) {
      leftTaskState.setRightTaskStateId(rightTaskState.getId());
      rightTaskState.setLeftTaskStateId(leftTaskState.getId());
      stateRepo.save(leftTaskState);
      stateRepo.save(rightTaskState);
    }
    else if (leftTaskState != null) {
      leftTaskState.setRightTaskStateId(null);
      stateRepo.save(leftTaskState);
    }
    else if (rightTaskState != null) {
      rightTaskState.setLeftTaskStateId(null);
      stateRepo.save(rightTaskState);
    }

    stateRepo.deleteById(id);
    return Boolean.TRUE;
  }

  @PutMapping(CREATE_TASK_STATE)
  @Transactional
  public TaskStateDTO createTaskState(
          @RequestParam("state_name") String stateName,
          @PathVariable("project_id") Long projectId) {

    if (stateName.trim().isEmpty()) {
      throw new BadRequestException("Task state name cannot be empty");
    }

    Project project = projectRepo
            .findById(projectId)
            .orElseThrow(() -> new NotFoundException("Project with id " + projectId + " not found"));

    Optional<TaskState> lastTaskState = project.getTaskStates().stream()
            .filter(state -> state.getRightTaskStateId() == null)
            .findFirst();

    TaskState taskState = stateRepo.save(TaskState.builder()
            .stateName(stateName)
            .build());

    lastTaskState.map(state -> {
      taskState.setLeftTaskStateId(state.getId());
      state.setRightTaskStateId(taskState.getId());
      return null;
    });

    project.getTaskStates().add(taskState);
    projectRepo.save(project);
    return taskStateMapper.toTaskStateDTO(taskState);
  }


  @PatchMapping(UPDATE_TASK_STATE)
  @Transactional
  public TaskStateDTO updateTaskState(@PathVariable Long id,
                                      @RequestParam("state_name") String stateName) {

    TaskState taskState = stateRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("Project with id " + id + " not found"));

    taskState.setStateName(stateName);
    taskState.setUpdatedAt(Instant.now());
    return taskStateMapper.toTaskStateDTO(stateRepo.save(taskState));
  }


  @PatchMapping(CHANGE_STATE_POSITION)
  @Transactional
  public Boolean changeStatePosition(@PathVariable("project_id") Long projectId,
                                     @RequestParam("state_id") Long stateId,
                                     @RequestParam("another_state_id") Long anotherStateId) {

    Project project = projectRepo.findById(projectId)
            .orElseThrow(() -> new NotFoundException("Project with id " + projectId + " not found"));

    TaskState taskState = project.getTaskStates().stream()
            .filter(state -> state.getId().equals(stateId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Task state with id " + stateId + " not found"));

    TaskState anotherState = project.getTaskStates()
            .stream()
            .filter(state -> state.getId().equals(anotherStateId)).findFirst()
            .orElseThrow(() -> new NotFoundException("Task state with id " + anotherStateId + " not found"));

    Optional<Long> leftStateId = Optional.ofNullable(taskState.getLeftTaskStateId());
    Optional<Long> rightStateId = Optional.ofNullable(taskState.getRightTaskStateId());
    Optional<Long> anotherRightStateId = Optional.ofNullable(anotherState.getRightTaskStateId());
    Optional<Long> anotherLeftStateId = Optional.ofNullable(anotherState.getLeftTaskStateId());

    if (anotherLeftStateId.isPresent() && anotherLeftStateId.get().equals(stateId)) {
      anotherState.setLeftTaskStateId(leftStateId.orElse(null));
      anotherState.setRightTaskStateId(stateId);
      taskState.setLeftTaskStateId(anotherStateId);
      taskState.setRightTaskStateId(anotherRightStateId.orElse(null));
    }
    else if (anotherRightStateId.isPresent() && anotherRightStateId.get().equals(stateId)) {
      anotherState.setRightTaskStateId(rightStateId.orElse(null));
      anotherState.setLeftTaskStateId(stateId);
      taskState.setRightTaskStateId(anotherStateId);
      taskState.setLeftTaskStateId(anotherLeftStateId.orElse(null));
    }
    else {
      anotherState.setLeftTaskStateId(leftStateId.orElse(null));
      anotherState.setRightTaskStateId(rightStateId.orElse(null));
      taskState.setLeftTaskStateId(anotherLeftStateId.orElse(null));
      taskState.setRightTaskStateId(anotherRightStateId.orElse(null));
    }

    taskState.setUpdatedAt(Instant.now());
    anotherState.setUpdatedAt(Instant.now());
    projectRepo.save(project);
    return Boolean.TRUE;
  }
}
