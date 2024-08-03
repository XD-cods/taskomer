package com.example.taskomer.services;

import com.example.taskomer.model.Task;
import com.example.taskomer.repositories.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
  private TaskRepo taskRepo;

  @Autowired
  public TaskService(TaskRepo taskRepo) {
    this.taskRepo = taskRepo;
  }

  public List<Task> getAllTasks() {
    return taskRepo.findAll();
  }

  public Task getTaskById(int id) {
    return taskRepo.findById(id).orElse(null);
  }

  public Task createTask(Task task) {
    return taskRepo.save(task);
  }

  public Task updateTask(Task task) {
    return taskRepo.save(task);
  }

  public void deleteTask(int id) {
    taskRepo.deleteById(id);
  }
}
