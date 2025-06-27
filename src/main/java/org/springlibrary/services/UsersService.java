package org.springlibrary.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springlibrary.dtos.users.LoginUserDTO;
import org.springlibrary.dtos.users.RegisterUserDTO;
import org.springlibrary.dtos.users.UserResponseDTO;
import org.springlibrary.entities.Role;
import org.springlibrary.entities.User;
import org.springlibrary.exceptions.DuplicateUserException;
import org.springlibrary.exceptions.RoleNotFoundException;
import org.springlibrary.exceptions.UserNotFoundException;
import org.springlibrary.mappers.UserMapper;
import org.springlibrary.repositories.RolesRepository;
import org.springlibrary.repositories.UsersRepository;

import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LibraryUsersDetailsService libraryUsersDetailsService;

    public UsersService(UsersRepository usersRepository,
                        PasswordEncoder passwordEncoder,
                        RolesRepository rolesRepository,
                        JwtService jwtService,
                        LibraryUsersDetailsService libraryUsersDetailsService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
        this.jwtService = jwtService;
        this.libraryUsersDetailsService = libraryUsersDetailsService;
    }

    @Transactional
    public String register(RegisterUserDTO dto) {
        if (usersRepository.existsByUsername(dto.getUsername()))
            throw new DuplicateUserException("error.user.duplicate");

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        User user = UserMapper.toEntity(dto);

        Role userRole = rolesRepository.findByName("USER")
                .orElseThrow(() -> new RoleNotFoundException("error.role.notfound"));
        user.setRoles(List.of(userRole));

        usersRepository.save(user);

        return jwtService.generateToken(user);
    }

    @Transactional
    public String login(LoginUserDTO dto) {
        UserDetails userDetails = libraryUsersDetailsService.loadUserByUsername(dto.getUsername());

        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
            throw new UserNotFoundException("error.user.notfound");
        }

        User user = usersRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UserNotFoundException("error.user.notfound"));

        return jwtService.generateToken(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return usersRepository.findAll().stream().map(UserMapper::toDTO).toList();
    }
}
