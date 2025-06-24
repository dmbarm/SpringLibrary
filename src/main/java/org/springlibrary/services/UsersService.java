package org.springlibrary.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springlibrary.dtos.users.RegisterUserDTO;
import org.springlibrary.exceptions.DuplicateUserException;
import org.springlibrary.mappers.UserMapper;
import org.springlibrary.repositories.UsersRepository;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository,
                        PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegisterUserDTO dto) {
        if (usersRepository.existsByUsername(dto.getUsername()))
            throw new DuplicateUserException("error.user.duplicate");

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        usersRepository.save(UserMapper.toEntity(dto));
    }
}
