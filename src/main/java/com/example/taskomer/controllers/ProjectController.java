package com.example.taskomer.controllers;

import com.example.taskomer.responses.ProjectResponse;
import com.example.taskomer.services.ProjectService;
import jakarta.transaction.Transactional;
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
public class ProjectController {
  private static final String SHOW_PROJECTS = "/api/projects";
  private static final String DELETE_PROJECT = "/api/projects/{id}";
  private static final String UPDATE_PROJECT = "/api/projects";
  private static final String CREATE_PROJECT = "/api/projects";
  private final ProjectService projectService;

  @PutMapping(CREATE_PROJECT)
  public ProjectResponse createProject(@RequestParam("project_name") String projectName) {

    return projectService.createProject(projectName);
  }

  @PatchMapping(UPDATE_PROJECT)
  @Transactional
  public ProjectResponse updateProject(@RequestParam("project_name") String projectName,
                                       @RequestParam(value = "project_id") Long projectId) {

    return projectService.updateProject(projectName, projectId);
  }

  @GetMapping(SHOW_PROJECTS)
  @Transactional
  public List<ProjectResponse> getAllProjects(
          @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

    return projectService.getAllProjects(optionalPrefixName);
  }

  @DeleteMapping(DELETE_PROJECT)
  @Transactional
  public Boolean deleteProject(@PathVariable("id") Long id) {

    return projectService.deleteProject(id);
  }
}
