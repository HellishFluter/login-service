package ru.weber.login.service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.weber.login.service.model.Role;
import ru.weber.login.service.repository.RoleRepository;
import ru.weber.login.service.service.RoleService;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final int USER_ROLE_ID = 2;

    @Transactional
    @Override
    public Role getDefaultRole() {
        return roleRepository.findById(USER_ROLE_ID).get();
    }
}
