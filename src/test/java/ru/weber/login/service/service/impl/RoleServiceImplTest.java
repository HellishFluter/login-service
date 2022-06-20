package ru.weber.login.service.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.weber.login.service.model.Role;
import ru.weber.login.service.repository.RoleRepository;
import ru.weber.login.service.service.RoleService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RoleServiceImplTest {
    private RoleService roleService;
    @MockBean
    private RoleRepository roleRepository;
    private final int USER_ROLE_ID = 2;
    private final String ROLE_NAME = "User";

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    void getDefaultRole_Ok() {
        when(roleRepository.findById(USER_ROLE_ID)).thenReturn(Optional.of(Role.builder().name(ROLE_NAME).build()));
        assertEquals(ROLE_NAME, roleService.getDefaultRole().getName());
    }
}