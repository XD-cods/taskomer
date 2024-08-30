package com.example.taskomer.util.mappers;

import com.example.taskomer.model.User;
import com.example.taskomer.responses.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserResponse toResponse(User user) {
    return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .build();
  }
}
