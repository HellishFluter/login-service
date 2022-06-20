package ru.weber.login.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginDto {
    @NotNull
    private String login;
    @NotNull
    private String password;
}
