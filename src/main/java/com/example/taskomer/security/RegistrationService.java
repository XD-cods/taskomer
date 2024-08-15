package com.example.taskomer.security;


import com.example.taskomer.model.User;
import com.example.taskomer.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepo userRepo;

  public User registerUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepo.save(user);
  }
}
