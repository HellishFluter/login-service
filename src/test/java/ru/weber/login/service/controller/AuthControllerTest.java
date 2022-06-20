package ru.weber.login.service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import ru.weber.login.service.config.SecurityConfig;
import ru.weber.login.service.exception.UserNotFoundException;
import ru.weber.login.service.service.JwtService;
import ru.weber.login.service.service.RoleService;
import ru.weber.login.service.service.UserService;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserService userService;
    @MockBean
    private RoleService roleService;
    @MockBean
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private AuthController authController;

    private final String LOGIN = "admin";

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        authController = new AuthController(authenticationManager, userService, jwtService);
        when(jwtService.generateToken(any())).thenReturn("stub");
    }

    @Test
    void authenticateUser_Ok() throws Exception {
        when(userDetailsService.loadUserByUsername(LOGIN)).thenReturn(
                new org.springframework.security.core.userdetails.User(
                "admin",
                passwordEncoder.encode(LOGIN),
                Collections.singleton(new SimpleGrantedAuthority("ADMIN")))
        );

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/auth/signin")
                        .content("{ \"login\": \"admin\", \"password\": \"admin\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{\"type\":\"Bearer\",\"token\":\"stub\"}"));
    }

    @Test
    void authenticateUser_badPass() {

        when(userDetailsService.loadUserByUsername(LOGIN)).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        LOGIN,
                        passwordEncoder.encode("admin"),
                        Collections.singleton(new SimpleGrantedAuthority("ADMIN")))
        );

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        NestedServletException ex = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/auth/signin")
                    .content("{ \"login\": \"admin\", \"password\": \"bad\"}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));
        });
        assertTrue(ex.getMessage().contains("BadCredentialsException"));
    }

    @Test
    void authenticateUser_badUser() throws Exception {

        when(userDetailsService.loadUserByUsername(LOGIN)).thenThrow(new UserNotFoundException(LOGIN));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/auth/signin")
                        .content("{ \"login\": \"admin\", \"password\": \"bad\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_Ok() throws Exception {
        when(userService.existsByLogin(LOGIN)).thenReturn(false);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/auth/signup")
                        .content("{\n" +
                                "    \"firstName\": \"Иван\",\n" +
                                "    \"surname\": \"Петров\",\n" +
                                "    \"middleName\": \"Петрович\",\n" +
                                "    \"login\": \"admin\",\n" +
                                "    \"password\": \"admin\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{\"type\":\"Bearer\",\"token\":\"stub\"}"));
    }

    @Test
    void registerUser_UserExist() throws Exception {
        when(userService.existsByLogin(LOGIN)).thenReturn(true);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/auth/signup")
                        .content("{\n" +
                                "    \"firstName\": \"Иван\",\n" +
                                "    \"surname\": \"Петров\",\n" +
                                "    \"middleName\": \"Петрович\",\n" +
                                "    \"login\": \"admin\",\n" +
                                "    \"password\": \"admin\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}