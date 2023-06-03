package com.example.security.resources;

import com.example.security.domain.SecurityUser;
import com.example.security.domain.User;
import com.example.security.domain.dto.AuthSuccessDTO;
import com.example.security.service.JpaUserDetailsService;
import com.example.security.service.TokenService;
import com.example.security.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthResource {

  private final TokenService tokenService;
  private final UserService userService;
  private final JpaUserDetailsService jpaUserDetailsService;

  public AuthResource(TokenService tokenService, UserService userService, JpaUserDetailsService jpaUserDetailsService) {
    this.tokenService = tokenService;
    this.userService = userService;
    this.jpaUserDetailsService = jpaUserDetailsService;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthSuccessDTO> login(Authentication authentication) {
    String token = tokenService.generateToken(authentication);
    User user = userService.findByUsername(authentication.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong"));
    AuthSuccessDTO authSuccess = new AuthSuccessDTO(token, user.toDto());
    return new ResponseEntity<>(authSuccess, HttpStatus.OK);
  }

  @PostMapping("/signup")
  public ResponseEntity<AuthSuccessDTO> signup(@RequestBody Map<String, Object> signupJson) {
    User newUser = userService.signup(signupJson);
    SecurityUser securityUser = (SecurityUser) jpaUserDetailsService.loadUserByUsername(newUser.getUsername());
    String token = tokenService.generateToken(securityUser);
    AuthSuccessDTO authSuccess = new AuthSuccessDTO(token, securityUser.getUser().toDto());
    return new ResponseEntity<>(authSuccess, HttpStatus.OK);
  }

  @GetMapping("/authorities")
  public Map<String, Object> getPrincipalInfo(JwtAuthenticationToken principal) {

    Collection<String> authorities = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

    Map<String, Object> info = new HashMap<>();
    info.put("name", principal.getName());
    info.put("authorities", authorities);
    info.put("tokenAttributes", principal.getTokenAttributes());

    return info;
  }
}
