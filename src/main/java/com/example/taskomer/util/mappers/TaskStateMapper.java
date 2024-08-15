package com.example.taskomer.util.mappers;

import com.example.taskomer.model.TaskState;
import com.example.taskomer.responses.TaskStateResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskStateMapper {
  public TaskStateResponse toResponse(TaskState taskState) {
    return TaskStateResponse.builder()
            .id(taskState.getId())
            .stateName(taskState.getStateName())
            .createdAt(taskState.getCreatedAt())
            .updatedAt(taskState.getUpdatedAt())
            .leftTaskStateId(taskState.getLeftTaskStateId())
            .rightTaskStateId(taskState.getRightTaskStateId())
            .build();
  }
}
