package com.example.taskomer.controllers;

import com.example.taskomer.DTO.ProjectDTO;
import com.example.taskomer.exceptions.NotFoundException;
import com.example.taskomer.model.Project;
import com.example.taskomer.repositories.ProjectRepo;
import com.example.taskomer.util.mappers.ProjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class ProjectController {
  private static final String SHOW_PROJECTS = "/api/projects";
  private static final String DELETE_PROJECT = "/api/projects/{id}";
  private static final String UPDATE_PROJECT = "/api/projects";
  private static final String CREATE_PROJECT = "/api/projects";
  private final ProjectRepo projectRepo;
  private final ProjectMapper projectMapper;

  @PutMapping(CREATE_PROJECT)
  @Transactional
  public ProjectDTO createProject(@RequestParam("project_name") String projectName) {

    Project project = Project.builder()
            .projectName(projectName)
            .build();

    projectRepo.save(project);
    return projectMapper.toDto(project);
  }

  @PatchMapping(UPDATE_PROJECT)
  @Transactional
  public ProjectDTO updateProject(@RequestParam("project_name") String projectName,
                                  @RequestParam(value = "project_id") Long projectId) {

    Project project = projectRepo.findById(projectId)
            .orElseThrow(() -> new NotFoundException("Project with id" + projectId + " found"));

    project.setProjectName(projectName);
    project.setUpdatedAt(Instant.now());
    return projectMapper.toDto(projectRepo.save(project));
  }


  @GetMapping(SHOW_PROJECTS)
  @Transactional
  public List<ProjectDTO> getAllProjects(
          @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

    optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

    Stream<Project> projectStream = optionalPrefixName
            .map(projectRepo::streamAllByProjectNameStartsWithIgnoreCase)
            .orElseGet(projectRepo::streamAllBy);

    return projectStream
            .map(projectMapper::toDto)
            .collect(Collectors.toList());
  }


  @DeleteMapping(DELETE_PROJECT)
  @Transactional
  public Boolean deleteProject(@PathVariable("id") Long id) {

    Project project = projectRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("Project not found"));

    projectRepo.delete(project);
    return Boolean.TRUE;
  }
}
