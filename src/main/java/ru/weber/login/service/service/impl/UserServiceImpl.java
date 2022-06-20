package ru.weber.login.service.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.weber.login.service.dto.SignUpDto;
import ru.weber.login.service.exception.UserNotFoundException;
import ru.weber.login.service.model.User;
import ru.weber.login.service.repository.UserRepository;
import ru.weber.login.service.service.RoleService;
import ru.weber.login.service.service.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Transactional
    @Override
    public boolean existsByLogin(String login) {
       return userRepository.existsByLogin(login);
    }

    @Transactional
    @Override
    public User save(SignUpDto signUpDto) {
        return createUserFromDto(signUpDto);
    }

    @Transactional
    @Override
    public User getByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
    }

    private User createUserFromDto(SignUpDto signUpDto) {
        User user = modelMapper.map(signUpDto, User.class);
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setRole(roleService.getDefaultRole());
        return user;
    }
}
