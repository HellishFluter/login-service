package ru.weber.login.service.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.weber.login.service.exception.UserNotFoundException;
import ru.weber.login.service.model.Role;
import ru.weber.login.service.model.User;
import ru.weber.login.service.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserDetailsServiceImplTest {
    private UserDetailsService userDetailsService;
    @MockBean
    private UserRepository userRepository;
    private final String GOOD_LOGIN = "Good_login";
    private final String BAD_LOGIN = "Bad_login";
    private final String PASSWORD = "password";

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void loadUserByUsername_Ok() {
        when(userRepository.findByLogin(GOOD_LOGIN)).thenReturn(Optional.of(
                User.builder()
                        .login(GOOD_LOGIN)
                        .password(PASSWORD)
                        .role(Role.builder().name("admin").build())
                        .build())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(GOOD_LOGIN);
        assertEquals(GOOD_LOGIN, userDetails.getUsername());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void loadUserByUsername_NotFound() {
        when(userRepository.findByLogin(BAD_LOGIN)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userDetailsService.loadUserByUsername(GOOD_LOGIN));
    }
}