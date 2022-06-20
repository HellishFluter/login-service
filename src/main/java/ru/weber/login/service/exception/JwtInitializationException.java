package ru.weber.login.service.exception;

public class JwtInitializationException extends RuntimeException {
    public JwtInitializationException(Throwable e) {
        super("Dont read private key!", e);
    }
}
