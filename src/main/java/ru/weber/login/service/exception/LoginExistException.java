package ru.weber.login.service.exception;

public class LoginExistException extends RuntimeException{
    public LoginExistException(String login) {
        super("Login '" + login + "' already exists");
    }
}
