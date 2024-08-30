package com.example.taskomer.controllers;

import com.example.taskomer.responses.TaskStateResponse;
import com.example.taskomer.services.TaskStateService;
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
public class TaskStateController {
  private static final String CREATE_TASK_STATE = "/api/states/{project_id}";
  private static final String UPDATE_TASK_STATE = "/api/states/{id}";
  private static final String DELETE_TASK_STATE = "/api/states/{id}";
  private static final String SHOW_TASK_STATE = "/api/states";
  private static final String CHANGE_STATE_POSITION = "/api/states/{project_id}/position";
  private final TaskStateService taskStateService;


  @GetMapping(SHOW_TASK_STATE)
  public List<TaskStateResponse> getAllTaskStates(
          @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

    return taskStateService.getAllTaskStates(optionalPrefixName);
  }

  @DeleteMapping(DELETE_TASK_STATE)
  public Boolean deleteTaskState(@PathVariable Long id) {

    return taskStateService.deleteTaskState(id);
  }

  @PutMapping(CREATE_TASK_STATE)
  public TaskStateResponse createTaskState(
          @RequestParam("state_name") String stateName,
          @PathVariable("project_id") Long projectId) {

    return taskStateService.createTaskState(stateName, projectId);
  }

  @PatchMapping(UPDATE_TASK_STATE)
  public TaskStateResponse updateTaskState(@PathVariable Long id,
                                           @RequestParam("state_name") String stateName) {

    return taskStateService.updateTaskState(id, stateName);
  }

  @PatchMapping(CHANGE_STATE_POSITION)
  public Boolean changeStatePosition(@PathVariable("project_id") Long projectId,
                                     @RequestParam("state_id") Long stateId,
                                     @RequestParam("another_state_id") Long anotherStateId) {

    return taskStateService.changeStatePosition(projectId, stateId, anotherStateId);
  }
}
