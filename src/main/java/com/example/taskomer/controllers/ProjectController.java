package com.example.taskomer.controllers;

import com.example.taskomer.DTO.ProjectDTO;
import com.example.taskomer.exceptions.BadRequestException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

  public static final String CREATE_PROJECT = "/api/projects";
  public static final String SHOW_PROJECTS = "/api/projects";
  public static final String DELETE_PROJECT = "/api/projects/{id}";
  public static final String CREATE_OR_UPDATE_PROJECT = "/api/projects/{id}";


  @PostMapping(CREATE_PROJECT)
  public ProjectDTO createProject(@RequestParam("project_name") String projectName) {
    projectRepo.findByProjectName(projectName)
            .ifPresent(project -> {
              throw new BadRequestException("Project already exists");
            });

    Project project = new Project();
    project.setProjectName(projectName);
    return projectMapper.toDto(projectRepo.save(project));
  }

//  @PatchMapping(CREATE_OR_UPDATE_PROJECT)
//  public ProjectDTO updateProject(@RequestParam(value = "optionalProjectId",required = false) Optional<Long> optionalProjectId,
//                                  @RequestParam(value = "optionalProjectName",required = false) Optional<String> optionalProjectName) {
//    optionalProjectName = optionalProjectName.filter(projectName -> !projectName.trim().isEmpty());
//
//    boolean isCreated = optionalProjectId.isPresent();
//    if (!isCreated && !optionalProjectName.isPresent()) {
//      throw new BadRequestException("Project name is mandatory");
//    }
//
//    Project project = optionalProjectId
//            .map(projectRepo::findById)
//            .get()
//            .orElseGet(() -> Project.builder().build());
//  }

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
