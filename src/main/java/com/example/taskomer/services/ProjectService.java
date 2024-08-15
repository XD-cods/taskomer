package com.example.taskomer.services;


import com.example.taskomer.exceptions.NotFoundException;
import com.example.taskomer.model.Project;
import com.example.taskomer.repositories.ProjectRepo;
import com.example.taskomer.responses.ProjectResponse;
import com.example.taskomer.util.mappers.ProjectMapper;
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
public class ProjectService {
  private final ProjectRepo projectRepo;
  private final ProjectMapper projectMapper;

  @Transactional
  public ProjectResponse createProject(String projectName) {

    Project project = Project.builder()
            .projectName(projectName)
            .build();

    projectRepo.save(project);
    return projectMapper.toResponse(project);
  }

  @Transactional
  public ProjectResponse updateProject(String projectName, Long projectId) {

    Project project = projectRepo.findById(projectId)
            .orElseThrow(() -> new NotFoundException("Project with id" + projectId + " found"));

    project.setProjectName(projectName);
    project.setUpdatedAt(Instant.now());
    return projectMapper.toResponse(projectRepo.save(project));
  }


  @Transactional
  public List<ProjectResponse> getAllProjects(Optional<String> optionalPrefixName) {

    optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

    Stream<Project> projectStream = optionalPrefixName
            .map(projectRepo::streamAllByProjectNameStartsWithIgnoreCase)
            .orElseGet(projectRepo::streamAllBy);

    return projectStream
            .map(projectMapper::toResponse)
            .collect(Collectors.toList());
  }

  @Transactional
  public Boolean deleteProject(Long id) {

    Project project = projectRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("Project not found"));

    projectRepo.delete(project);
    return Boolean.TRUE;
  }
}
