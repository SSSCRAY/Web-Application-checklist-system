package com.example.backend.dto.security_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 150, message = "Имя от 2 до 150 символов")
    private String fullName;

    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 3, max = 100, message = "Логин от 3 до 100 символов")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 255, message = "Пароль минимум 6 символов")
    private String password;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}