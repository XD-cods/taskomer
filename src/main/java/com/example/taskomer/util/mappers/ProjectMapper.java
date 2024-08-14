package com.example.taskomer.util.mappers;

import com.example.taskomer.DTO.ProjectDTO;
import com.example.taskomer.model.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
  public ProjectDTO toDto(Project project) {
    return ProjectDTO.builder()
            .projectName(project.getProjectName())
            .updatedAt(project.getUpdatedAt())
            .id(project.getId())
            .createdAt(project.getCreatedAt())
            .build();
  }
}
