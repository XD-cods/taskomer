package com.example.taskomer.controllers;

import com.example.taskomer.responses.TaskResponse;
import com.example.taskomer.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TaskController {
  private static final String SHOW_TASKS = "/api/tasks";
  private static final String CREATE_TASK = "/api/tasks/{state_id}";
  private static final String UPDATE_TASK = "/api/tasks/{task_id}";
  private static final String DELETE_TASK = "/api/tasks/{task_id}";

  private final TaskService taskService;

  @GetMapping(SHOW_TASKS)
  public List<TaskResponse> showTasks(
          @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

    return taskService.showTasks(optionalPrefixName);
  }


  @PutMapping(CREATE_TASK)
  public TaskResponse createTask(@PathVariable("state_id") Long stateId,
                                 @RequestParam("task_name") String taskName,
                                 @RequestParam(value = "task_description", required = false)
                            Optional<String> optionalTaskDescription) {

    return taskService.createTask(stateId, taskName, optionalTaskDescription);
  }

  @PatchMapping(UPDATE_TASK)
  public TaskResponse updateTask(@PathVariable("task_id") Long taskId,
                                 @RequestParam(value = "task_name", required = false) Optional<String> taskName,
                                 @RequestParam(value = "task_description", required = false)
                            Optional<String> taskDescription) {

    return taskService.updateTask(taskId, taskName, taskDescription);
  }

  @DeleteMapping(DELETE_TASK)
  public Boolean deleteTask(@PathVariable("task_id") Long taskId) {

    return taskService.deleteTask(taskId);
  }

}
