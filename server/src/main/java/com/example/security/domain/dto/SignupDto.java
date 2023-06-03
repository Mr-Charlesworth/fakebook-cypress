package com.example.security.domain.dto;

import jakarta.validation.constraints.NotEmpty;

public class SignupDto {

    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Password is required")
    private String password;

    public SignupDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public SignupDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
