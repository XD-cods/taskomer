package com.example.taskomer.services;

import com.example.taskomer.model.User;
import com.example.taskomer.repositories.UserRepo;
import com.example.taskomer.responses.UserResponse;
import com.example.taskomer.util.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepo userRepo;
  private final UserMapper userMapper;

  public UserResponse createUser(User user) {
    return userMapper.toResponse(userRepo.save(user));
  }

  public Optional<User> findByUsername(String username) {
    return userRepo.findByName(username);
  }

}

