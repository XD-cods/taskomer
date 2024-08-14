package com.example.taskomer.DTO;

import com.example.taskomer.model.Project;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link Project}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO implements Serializable {

  @NonNull
  private Long id;

  @NonNull
  @JsonProperty("project_name")
  private String projectName;

  @NonNull
  @JsonProperty("updated_at")
  private Instant updatedAt;

  @NonNull
  @JsonProperty("created_at")
  private Instant createdAt;
}