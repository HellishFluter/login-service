package ru.weber.login.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.weber.login.service.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Boolean existsByLogin(String login);
}
