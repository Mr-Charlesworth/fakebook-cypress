package com.example.security.resources;

import com.example.security.domain.dto.AuthSuccessDTO;
import com.example.security.domain.dto.SignupDto;
import com.example.security.service.TokenService;
import com.example.security.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthResource {

    private final UserService userService;

    public AuthResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthSuccessDTO> login(Authentication authentication) {
        AuthSuccessDTO authSuccessDTO = userService.login(authentication);
        return new ResponseEntity<>(authSuccessDTO, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthSuccessDTO> signup(@Valid @RequestBody SignupDto signupJson) {
        AuthSuccessDTO authSuccess = userService.signup(signupJson);
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
