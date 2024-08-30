package com.example.taskomer.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

  ADMIN("ADMIN"),
  USER("USER");

  private final String val;

  @Override
  public String getAuthority() {
    return val;
  }
}
