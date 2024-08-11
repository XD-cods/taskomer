package com.example.taskomer.repositories;

import com.example.taskomer.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {
  Optional<Project> findByProjectName(String projectName);

  Stream<Project> streamAllBy();

  Stream<Project> streamAllByProjectNameStartsWithIgnoreCase(String projectName);
}
