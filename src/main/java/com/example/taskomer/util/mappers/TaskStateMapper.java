package com.example.taskomer.util.mappers;

import com.example.taskomer.DTO.TaskStateDTO;
import com.example.taskomer.model.TaskState;
import org.springframework.stereotype.Component;

@Component
public class TaskStateMapper {
  public TaskStateDTO toTaskStateDTO(TaskState taskState) {
    return TaskStateDTO.builder()
            .id(taskState.getId())
            .stateName(taskState.getStateName())
            .createdAt(taskState.getCreatedAt())
            .leftTaskStateId(taskState.getLeftTaskState() == null ? null : taskState.getLeftTaskState().getId())
            .rightTaskStateId(taskState.getRightTaskState() == null ? null : taskState.getRightTaskState().getId())
            .build();
  }
}
