package ru.weber.login.service.service;

import ru.weber.login.service.model.User;

public interface JwtService {
    String generateToken(User user);
}
