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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Transactional
@RequiredArgsConstructor
public class ProjectController {
  private final ProjectRepo projectRepo;
  private final ProjectMapper projectMapper;

  public static final String SHOW_PROJECTS = "/api/projects";
  public static final String DELETE_PROJECT = "/api/projects/{id}";
  public static final String CREATE_OR_UPDATE_PROJECT = "/api/projects";


  @PostMapping(CREATE_OR_UPDATE_PROJECT)
  public ProjectDTO updateProject(@RequestParam("project_name") String projectName,
                                  @RequestParam(value = "project_id", required = false) Optional<Long> projectId) {

    Project project = projectId
            .map(id -> projectRepo.findById(id).get())
            .orElseGet(() -> Project.builder()
                    .createdAt(Instant.now())
                    .build());

    project.setProjectName(projectName);
    return projectMapper.toDto(projectRepo.save(project));
  }


  @GetMapping(SHOW_PROJECTS)
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
  public Boolean deleteProject(@PathVariable("id") Long id) {
    Project project = projectRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("Project not found"));

    projectRepo.delete(project);
    return Boolean.TRUE;
  }


}
