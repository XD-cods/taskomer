package com.example.taskomer.DTO;

import com.example.taskomer.model.Project;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
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
  private String projectName;
  @JsonProperty("created_at")
  @NonNull
  private Instant createdAt;
}