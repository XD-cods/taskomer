package com.example.taskomer.util.mappers;


import com.example.taskomer.responses.TaskResponse;
import com.example.taskomer.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
  public TaskResponse toResponse(Task task) {
    return TaskResponse.builder()
            .id(task.getId())
            .taskName(task.getTaskName())
            .description(task.getDescription())
            .createdAt(task.getCreatedAt())
            .updatedAt(task.getUpdatedAt())
            .build();
  }
}
