package com.example.taskomer.util.mappers;

import com.example.taskomer.responses.ProjectResponse;
import com.example.taskomer.model.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
  public ProjectResponse toResponse(Project project) {
    return ProjectResponse.builder()
            .projectName(project.getProjectName())
            .updatedAt(project.getUpdatedAt())
            .id(project.getId())
            .createdAt(project.getCreatedAt())
            .build();
  }
}
