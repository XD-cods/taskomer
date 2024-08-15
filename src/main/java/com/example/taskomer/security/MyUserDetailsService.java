package com.example.taskomer.security;

import com.example.taskomer.model.User;
import com.example.taskomer.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

  private final UserRepo userRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userRepo.findByName(username)
            .orElseThrow(() -> new UsernameNotFoundException("User with username - " + username + " not found"));

    return new MyUserDetails(user);
  }
}
