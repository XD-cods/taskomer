package com.example.taskomer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(schema = "taskomer",name = "project")
public class Project {
  @Id
  @Column(name = "project_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String projectName;
  @Column(nullable = false)
  private Instant createdAt = Instant.now();
  @OneToMany
  @JoinColumn(name = "project_id", nullable = false)
  private List<TaskState> taskStates;
}
