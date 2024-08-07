package com.example.taskomer.DTO;

import com.example.taskomer.model.TaskState;
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
 * DTO for {@link TaskState}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStateDTO {
  @NonNull
  private Long id;
  @NonNull
  private String title;
  @JsonProperty("created_at")
  @NonNull
  private Instant createdAt;
}