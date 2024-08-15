package com.example.taskomer.controllers;

import com.example.taskomer.model.User;
import com.example.taskomer.security.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
  private static final String REGISTER_USER = "/api/auth/register";
  private final RegistrationService registrationService;

  @PutMapping(REGISTER_USER)
  public User register(@RequestParam String username,
                       @RequestParam String password) {

    return registrationService.registerUser(User.builder()
            .name(username)
            .password(password)
            .build());
  }
}
