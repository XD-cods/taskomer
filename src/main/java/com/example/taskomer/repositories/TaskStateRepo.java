package com.example.taskomer.repositories;

import com.example.taskomer.model.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface TaskStateRepo extends JpaRepository<TaskState, Long> {

  Stream<TaskState> streamAllByStateNameIgnoreCaseStartingWith(String stateName);

  Stream<TaskState> streamAllBy();
}
