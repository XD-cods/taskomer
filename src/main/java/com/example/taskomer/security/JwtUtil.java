package com.example.taskomer.security;

import com.example.taskomer.model.Role;
import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class JwtUtil {
  public static JwtAuthentication generate(Claims claims) {
    return JwtAuthentication.builder()
            .authorities(getRoles(claims))
            .name(claims.get("username", String.class))
            .build();

  }

  private static Set<Role> getRoles(Claims claims) {
    final Set<String> roles = Set.of(claims.get("roles", String.class));
    return roles.stream()
            .map(Role::valueOf)
            .collect(Collectors.toSet());
  }
}
