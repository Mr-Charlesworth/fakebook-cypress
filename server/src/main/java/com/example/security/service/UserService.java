package com.example.security.service;

import com.example.security.domain.User;
import com.example.security.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public static final String ADMIN_ROLE = "ROLE_ADMIN";

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  private User mapUserNoId(Map<String, Object> userJson) {
    String username = (String) userJson.getOrDefault("username", "");
    String password = (String) userJson.getOrDefault("password", "");
    User user = new User();
    user.setUsername(username);
    user.setPassword(password);
    return user;
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findUserByUsername(username);
  }

  private void validateNewUser(User newUser) {
    if (newUser.getUsername().isEmpty() || newUser.getPassword().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing username or password");
    }
    if (userRepository.existsUserByUsername(newUser.getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username not available");
    }
    if (newUser.getPassword().length() < 8) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters");
    }
  }

  public User signup(Map<String, Object> userJson) {
    User mappedUser = mapUserNoId(userJson);
    validateNewUser(mappedUser);
    mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
    mappedUser.setRoles("ROLE_USER");
    return userRepository.save(mappedUser);
  }
}
