package com.example.taskomer.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

  @NonNull
  private Long id;

  @NonNull
  private String name;
}
