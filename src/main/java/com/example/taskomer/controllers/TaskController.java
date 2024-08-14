package com.example.taskomer.controllers;

import com.example.taskomer.DTO.TaskDTO;
import com.example.taskomer.exceptions.BadRequestException;
import com.example.taskomer.exceptions.NotFoundException;
import com.example.taskomer.model.Task;
import com.example.taskomer.model.TaskState;
import com.example.taskomer.repositories.TaskRepo;
import com.example.taskomer.repositories.TaskStateRepo;
import com.example.taskomer.util.mappers.TaskMapper;
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
public class TaskController {
  private static final String SHOW_TASKS = "/api/tasks";
  private static final String CREATE_TASK = "/api/tasks/{state_id}";
  private static final String UPDATE_TASK = "/api/tasks/{task_id}";
  private static final String DELETE_TASK = "/api/tasks/{task_id}";

  private final TaskRepo taskRepo;
  private final TaskMapper taskMapper;
  private final TaskStateRepo taskStateRepo;

  @GetMapping(SHOW_TASKS)
  public List<TaskDTO> showTasks(
          @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

    optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

    Stream<Task> taskStream = optionalPrefixName
            .map(taskRepo::streamAllByTaskNameIgnoreCaseStartingWith)
            .orElseGet(taskRepo::streamAllBy);

    return taskStream
            .map(taskMapper::toDTO)
            .collect(Collectors.toList());
  }


  @PutMapping(CREATE_TASK)
  public TaskDTO createTask(@PathVariable("state_id") Long stateId,
                            @RequestParam("task_name") String taskName,
                            @RequestParam(value = "task_description", required = false)
                            Optional<String> optionalTaskDescription) {

    TaskState taskState = taskStateRepo.findById(stateId)
            .orElseThrow(() -> new NotFoundException("Task state with id" + stateId + " not found"));

    Task task = Task.builder()
            .taskName(taskName)
            .description(optionalTaskDescription.orElse(null))
            .build();

    taskState.getTasks().add(task);
    taskStateRepo.save(taskState);
    return taskMapper.toDTO(task);
  }

  @PatchMapping(UPDATE_TASK)
  public TaskDTO updateTask(@PathVariable("task_id") Long taskId,
                            @RequestParam(value = "task_name", required = false) Optional<String> taskName,
                            @RequestParam(value = "task_description", required = false)
                              Optional<String> taskDescription) {

    if (taskName.isEmpty() && taskDescription.isEmpty()) {
      throw new BadRequestException("Task name or description cannot be empty");
    }

    Task task = taskRepo.findById(taskId)
            .orElseThrow(() -> new NotFoundException("Task with id" + taskId + " not found"));

      taskName.ifPresent(task::setTaskName);
      taskDescription.ifPresent(task::setDescription);
      task.setUpdatedAt(Instant.now());
      return taskMapper.toDTO(task);
  }

  @DeleteMapping(DELETE_TASK)
  public Boolean deleteTask(@PathVariable("task_id") Long taskId) {
    taskStateRepo.findById(taskId)
            .orElseThrow(() -> new NotFoundException("Task with id" + taskId + " not found"));

    taskStateRepo.deleteById(taskId);
    return Boolean.TRUE;
  }

}
