package com.example.taskomer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(schema = "taskomer", name = "task")
public class Task {
  @Id
  @Column(name = "task_id", unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "task_title", nullable = false)
  private String title;
  @Column(name = "task_description", nullable = false)
  private String description;
  @Column(name = "task_status", nullable = false)
  private Status status;
}

