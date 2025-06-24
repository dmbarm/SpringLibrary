package org.springlibrary.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springlibrary.dtos.users.RegisterUserDTO;
import org.springlibrary.entities.Role;
import org.springlibrary.entities.User;
import org.springlibrary.exceptions.DuplicateUserException;
import org.springlibrary.exceptions.RoleNotFoundException;
import org.springlibrary.mappers.UserMapper;
import org.springlibrary.repositories.RolesRepository;
import org.springlibrary.repositories.UsersRepository;

import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository,
                        PasswordEncoder passwordEncoder,
                        RolesRepository rolesRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
    }

    @Transactional
    public void register(RegisterUserDTO dto) {
        if (usersRepository.existsByUsername(dto.getUsername()))
            throw new DuplicateUserException("error.user.duplicate");

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        User newUser = UserMapper.toEntity(dto);

        Role userRole = rolesRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("error.role.notfound"));
        newUser.setRoles(List.of(userRole));

        usersRepository.save(newUser);
    }
}
