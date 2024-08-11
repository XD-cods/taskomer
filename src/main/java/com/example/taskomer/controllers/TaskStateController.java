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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class TaskStateController {
  private static final String CREATE_TASK_STATE = "/api/states/{project_id}";
  private static final String UPDATE_TASK_STATE = "UPDATE_TASK_STATE";
  private static final String DELETE_TASK_STATE = "/api/states/{id}";
  private static final String SHOW_TASK_STATE = "/api/states";
  private final TaskStateRepo stateRepo;
  private final ProjectRepo projectRepo;
  private final TaskStateMapper taskStateMapper;

  @GetMapping(SHOW_TASK_STATE)
  @Transactional
  public List<TaskStateDTO> getAllTaskStates(
          @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName
  ) {

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
    stateRepo.findById(id).orElseThrow(() -> new NotFoundException("Task state with existence id not found"));

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

    Optional<TaskState> lastTaskState = Optional.empty();

    for (TaskState taskState : project.getTaskStates()) {
      if (taskState.getRightTaskState() == null) {
        lastTaskState = Optional.of(taskState);
      }
    }

    TaskState taskState = stateRepo.save(TaskState.builder()
            .stateName(stateName)
            .build());

    if (lastTaskState.isPresent()) {
      taskState.setLeftTaskState(lastTaskState.get());
      lastTaskState.get().setRightTaskState(taskState);
    }

    project.getTaskStates().add(taskState);
    projectRepo.save(project);

    return taskStateMapper.toTaskStateDTO(taskState);
  }
}
