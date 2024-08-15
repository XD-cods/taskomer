package com.example.taskomer.repositories;

import com.example.taskomer.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
  Stream<Task> streamAllBy();

  Stream<Task> streamAllByTaskNameIgnoreCaseStartingWith(String taskName);
}
