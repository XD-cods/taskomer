package com.example.taskomer.services;

import com.example.taskomer.responses.TaskResponse;
import com.example.taskomer.exceptions.BadRequestException;
import com.example.taskomer.exceptions.NotFoundException;
import com.example.taskomer.model.Task;
import com.example.taskomer.model.TaskState;
import com.example.taskomer.repositories.TaskRepo;
import com.example.taskomer.repositories.TaskStateRepo;
import com.example.taskomer.util.mappers.TaskMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepo taskRepo;
  private final TaskMapper taskMapper;
  private final TaskStateRepo taskStateRepo;

  @Transactional
  public List<TaskResponse> showTasks(Optional<String> optionalPrefixName) {

    optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

    Stream<Task> taskStream = optionalPrefixName
            .map(taskRepo::streamAllByTaskNameIgnoreCaseStartingWith)
            .orElseGet(taskRepo::streamAllBy);

    return taskStream
            .map(taskMapper::toResponse)
            .collect(Collectors.toList());
  }


  @Transactional
  public TaskResponse createTask(Long stateId, String taskName, Optional<String> optionalTaskDescription) {

    TaskState taskState = taskStateRepo.findById(stateId)
            .orElseThrow(() -> new NotFoundException("Task state with id" + stateId + " not found"));

    Task task = Task.builder()
            .taskName(taskName)
            .description(optionalTaskDescription.orElse(null))
            .build();

    taskState.getTasks().add(task);
    taskStateRepo.save(taskState);
    return taskMapper.toResponse(task);
  }

  @Transactional
  public TaskResponse updateTask(Long taskId, Optional<String> taskName, Optional<String> taskDescription) {

    if (taskName.isEmpty() && taskDescription.isEmpty()) {
      throw new BadRequestException("Task name or description cannot be empty");
    }

    Task task = taskRepo.findById(taskId)
            .orElseThrow(() -> new NotFoundException("Task with id" + taskId + " not found"));

    taskName.ifPresent(task::setTaskName);
    taskDescription.ifPresent(task::setDescription);
    task.setUpdatedAt(Instant.now());
    return taskMapper.toResponse(task);
  }

  @Transactional
  public Boolean deleteTask(Long taskId) {
    taskStateRepo.findById(taskId)
            .orElseThrow(() -> new NotFoundException("Task with id" + taskId + " not found"));

    taskStateRepo.deleteById(taskId);
    return Boolean.TRUE;
  }

}
