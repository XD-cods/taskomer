package com.example.taskomer.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(schema = "taskomer", name = "task_state")
public class TaskState {

  @Id
  @Column(name = "state_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String stateName;

  @Builder.Default
  private Instant createdAt = Instant.now();

  @OneToOne
  private TaskState leftTaskState;

  @OneToOne
  private TaskState rightTaskState;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "state_id")
  @Builder.Default
  private List<Task> tasks = new ArrayList<>();
}