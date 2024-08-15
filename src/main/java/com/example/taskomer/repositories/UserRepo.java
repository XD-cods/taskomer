package com.example.taskomer.repositories;

import com.example.taskomer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
  Optional<User> findByName(String username);
}
