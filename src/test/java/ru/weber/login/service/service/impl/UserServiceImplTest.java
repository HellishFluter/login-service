package ru.weber.login.service.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.weber.login.service.dto.SignUpDto;
import ru.weber.login.service.exception.UserNotFoundException;
import ru.weber.login.service.model.User;
import ru.weber.login.service.repository.UserRepository;
import ru.weber.login.service.service.RoleService;
import ru.weber.login.service.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    private UserService userService;
    private ModelMapper modelMapper;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleService roleService;

    private final String GOOD_LOGIN = "Good_login";
    private final String BAD_LOGIN = "Bad_login";
    private final String PASSWORD = "password";

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        userService = new UserServiceImpl(userRepository, modelMapper, passwordEncoder, roleService);
    }

    @Test
    void existsByLogin() {
        when(userRepository.existsByLogin(GOOD_LOGIN)).thenReturn(false);
        when(userRepository.existsByLogin(BAD_LOGIN)).thenReturn(true);

        assertFalse(userRepository.existsByLogin(GOOD_LOGIN));
        assertTrue(userRepository.existsByLogin(BAD_LOGIN));
    }

    @Test
    void save() {
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        User user = userService.save(SignUpDto
                .builder()
                .login(GOOD_LOGIN)
                .password(PASSWORD)
                .firstName("John")
                .lastName("Smith")
                .build()
        );
        assertEquals(GOOD_LOGIN, user.getLogin());
        verify(roleService, times(1)).getDefaultRole();
        verify(passwordEncoder,times(1)).encode(PASSWORD);
    }

    @Test
    void getByLogin_ok() {
        when(userRepository.findByLogin(GOOD_LOGIN)).thenReturn(Optional.of(new User()));
        assertDoesNotThrow(() -> userService.getByLogin(GOOD_LOGIN));
    }

    @Test
    void getByLogin_notFound() {
        when(userRepository.findByLogin(GOOD_LOGIN)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getByLogin(GOOD_LOGIN));
    }

}