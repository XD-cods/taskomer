package com.example.taskomer.security;

import com.example.taskomer.exceptions.BadRequestException;
import com.example.taskomer.exceptions.NotFoundException;
import com.example.taskomer.model.User;
import com.example.taskomer.responses.JwtResponse;
import com.example.taskomer.responses.UserResponse;
import com.example.taskomer.services.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserService userService;
  private final JwtProvider jwtProvider;
  private final PasswordEncoder passwordEncoder;
  private final HashMap<String, String> refreshStorage = new HashMap<>();

  public UserResponse register(String username, String password) {
    userService.findByUsername(username)
            .ifPresent(user -> {
              throw new BadRequestException("Username already exists");
            });

    User user = User.builder()
            .name(username)
            .password(passwordEncoder.encode(password))
            .build();

    return userService.createUser(user);
  }

  public JwtResponse login(String username, String password) {
    User user = userService.findByUsername(username)
            .orElseThrow(() -> new NotFoundException("User not found"));

    if (passwordEncoder.matches(password, user.getPassword())) {
      String accessToken = jwtProvider.generateAccessToken(user);
      String refreshToken = jwtProvider.generateRefreshToken(user);

      refreshStorage.put(username, refreshToken);
      return new JwtResponse(accessToken, refreshToken);
    } else {
      throw new BadRequestException("Wrong password");
    }
  }

  public JwtResponse refreshToken(String refreshToken) {
    if (jwtProvider.validateRefreshToken(refreshToken)) {
      Claims claims = jwtProvider.getRefreshClaims(refreshToken);
      String login = claims.get("sub", String.class);

      String savedRefreshToken = refreshStorage.get(login);
      if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
        User user = userService.findByUsername(login)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String accessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);

        refreshStorage.put(login, newRefreshToken);
        return new JwtResponse(accessToken, newRefreshToken);
      }
    }
    throw new BadRequestException("not a valid token");
  }

  public JwtResponse getAccessToken(String refreshToken) {
    if (jwtProvider.validateRefreshToken(refreshToken)) {
      Claims claims = jwtProvider.getRefreshClaims(refreshToken);
      String login = claims.get("sub", String.class);

      String savedRefreshToken = refreshStorage.get(login);
      if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
        User user = userService.findByUsername(login)
                .orElseThrow(() -> new NotFoundException("User not found"));
        String accessToken = jwtProvider.generateAccessToken(user);

        return new JwtResponse(accessToken, null);
      }
    }
    throw new BadRequestException("not a valid token");
  }
}