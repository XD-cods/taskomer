package com.example.taskomer.responses;

import com.example.taskomer.model.TaskState;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;

/**
 * DTO for {@link TaskState}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStateResponse {

  @NonNull
  private Long id;

  @NonNull
  @JsonProperty("state_name")
  private String stateName;

  @NonNull
  @JsonProperty("updated_at")
  private Instant updatedAt;

  @NonNull
  @JsonProperty("created_at")
  private Instant createdAt;

  @JsonProperty("left_task_state_id")
  private Long leftTaskStateId;

  @JsonProperty("right_task_state_id")
  private Long rightTaskStateId;
}