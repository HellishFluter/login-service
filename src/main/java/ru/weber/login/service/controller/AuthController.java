package ru.weber.login.service.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.weber.login.service.dto.LoginDto;
import ru.weber.login.service.dto.SignUpDto;
import ru.weber.login.service.dto.Token;
import ru.weber.login.service.exception.LoginExistException;
import ru.weber.login.service.model.User;
import ru.weber.login.service.service.JwtService;
import ru.weber.login.service.service.RoleService;
import ru.weber.login.service.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    private final JwtService jwtService;

    private static final String AUTHORIZATION_BEARER = "Bearer";

    @PostMapping("/signin")
    public ResponseEntity<Token> authenticateUser(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getLogin(),
                loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.getByLogin(loginDto.getLogin());
        return new ResponseEntity<>(new Token(AUTHORIZATION_BEARER, jwtService.generateToken(user)), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {
        if (userService.existsByLogin(signUpDto.getLogin())) {
            throw new LoginExistException(signUpDto.getLogin());
        }
        User user = userService.save(signUpDto);
        return new ResponseEntity<>(new Token(AUTHORIZATION_BEARER, jwtService.generateToken(user)), HttpStatus.OK);
    }
}
