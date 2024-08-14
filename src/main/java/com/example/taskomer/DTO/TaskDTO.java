package com.example.taskomer.DTO;

import com.example.taskomer.model.Task;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;

/**
 * DTO for {@link Task}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

  @NonNull
  private Long id;

  @NonNull
  private String taskName;

  @NonNull
  private String description;

  @NonNull
  @JsonProperty("updated_at")
  private Instant updatedAt;

  @NonNull
  @JsonProperty("created_at")
  private Instant createdAt;
}