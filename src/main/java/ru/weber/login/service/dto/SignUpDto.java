package ru.weber.login.service.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class SignUpDto {
    /**Имя*/
    @NotNull
    private String firstName;
    /**Фамилия*/
    @NotNull
    private String lastName;
    /**Отчетсво*/
    private String middleName;
    /**Логин*/
    @NotNull
    private String login;
    /**Пароль*/
    @NotNull
    private String password;
}
