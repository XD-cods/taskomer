package com.example.taskomer.model;

import jakarta.persistence.CascadeType;
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
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(schema = "taskomer", name = "project")
public class Project {

  @Id
  @Column(name = "project_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String projectName;

  @Builder.Default
  private Instant createdAt = Instant.now();

  @OneToMany(cascade = CascadeType.ALL)
  @Builder.Default
  @JoinColumn(name = "project_id")
  private List<TaskState> taskStates = new ArrayList<>();
}
