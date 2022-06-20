package ru.weber.login.service.service;

import ru.weber.login.service.dto.SignUpDto;
import ru.weber.login.service.model.User;

public interface UserService {

    boolean existsByLogin(String login);

    User save(SignUpDto signUpDto);

    User getByLogin(String login);
}
