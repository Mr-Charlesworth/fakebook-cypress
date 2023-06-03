package com.example.security.service;

import com.example.security.domain.SecurityUser;
import com.example.security.domain.User;
import com.example.security.domain.dto.AuthSuccessDTO;
import com.example.security.domain.dto.SignupDto;
import com.example.security.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;
  private final JpaUserDetailsService jpaUserDetailsService;

  public static final String ADMIN_ROLE = "ROLE_ADMIN";
  public static final String USER_ROLE = "ROLE_USER";

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService, JpaUserDetailsService jpaUserDetailsService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenService = tokenService;
    this.jpaUserDetailsService = jpaUserDetailsService;
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findUserByUsername(username);
  }

  public AuthSuccessDTO signup(SignupDto userJson) {
    if (userRepository.existsUserByUsername(userJson.getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username not available");
    }
    User user = new User(userJson.getUsername(), passwordEncoder.encode(userJson.getPassword()), USER_ROLE);
    userRepository.save(user);
    SecurityUser securityUser = (SecurityUser) jpaUserDetailsService.loadUserByUsername(user.getUsername());
    String token = tokenService.generateToken(securityUser);
    return new AuthSuccessDTO(token, securityUser.getUser().toDto());
  }

  public AuthSuccessDTO login(Authentication authentication) {
    String token = tokenService.generateToken(authentication);
    User user = userRepository.findUserByUsername(authentication.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    return new AuthSuccessDTO(token, user.toDto());
  }
}
