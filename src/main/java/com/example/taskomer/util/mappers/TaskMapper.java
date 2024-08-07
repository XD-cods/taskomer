package com.example.taskomer.util.mappers;


import com.example.taskomer.DTO.TaskDTO;
import com.example.taskomer.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
  public TaskDTO toDTO(Task task) {
    return TaskDTO.builder()
            .id(task.getId())
            .taskName(task.getTaskName())
            .description(task.getDescription())
            .createdAt(task.getCreatedAt())
            .build();
  }
}
