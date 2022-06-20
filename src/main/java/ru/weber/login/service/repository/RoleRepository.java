package ru.weber.login.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.weber.login.service.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
