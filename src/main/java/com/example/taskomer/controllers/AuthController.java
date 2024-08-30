package com.example.taskomer.controllers;

import com.example.taskomer.responses.JwtResponse;
import com.example.taskomer.responses.UserResponse;
import com.example.taskomer.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
  private static final String REGISTER_USER = "/api/auth/register";
  private static final String LOGIN = "/api/auth/login";
  private static final String GET_ACCESS_TOKEN = "/api/auth/access";
  private static final String GET_REFRESH_TOKEN = "/api/auth/refresh";
  private final AuthService authService;

  @PutMapping(REGISTER_USER)
  public UserResponse register(@RequestParam String username,
                               @RequestParam String password) {

    return authService.register(username, password);
  }

  @GetMapping(LOGIN)
  public JwtResponse login(@RequestParam String username,
                           @RequestParam String password) {

    return authService.login(username, password);
  }

  @GetMapping(GET_ACCESS_TOKEN)
  public JwtResponse getAccessToken(@RequestParam("refresh_token") String refreshToken) {

    return authService.getAccessToken(refreshToken);
  }

  @GetMapping(GET_REFRESH_TOKEN)
  public JwtResponse getRefreshToken(@RequestParam("refresh_token") String refreshToken) {
    return authService.refreshToken(refreshToken);
  }
}
