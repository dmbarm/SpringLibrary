package org.springlibrary.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springlibrary.dtos.users.LoginUserDTO;
import org.springlibrary.dtos.users.RegisterUserDTO;
import org.springlibrary.dtos.users.UserResponseDTO;
import org.springlibrary.entities.Role;
import org.springlibrary.entities.User;
import org.springlibrary.repositories.RolesRepository;
import org.springlibrary.repositories.UsersRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {
    @Mock
    private UsersRepository usersRepository;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private LibraryUsersDetailsService libraryUsersDetailsService;

    @InjectMocks
    private UsersService usersService;

    @Test
    void shouldRegisterUserSuccessfully() {
        String username = "TestUsername";
        String password = "TestPassword";
        String hashedPassword = "$2a$10$zIkMNb0CO5WToEC77/SOs.QYMfUV066ZZQN9sMnChQgnGYmulzlvy";
        String jwtString = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJUZXN0TElicmFyeSIsImlhdCI6MTc1MTIzMTY0MSwiZXhwIjo" +
                "xNzgyNzY3NjQxLCJhdWQiOiJ3d3cuZXhhbXBsZS5jb20iLCJzdWIiOiJsaWJyYXJ5QGV4YW1wbGUuY29tIiwiR2l2ZW5OYW1lIjoi" +
                "Sm9obm55IiwiU3VybmFtZSI6IlJvY2tldCIsIlJvbGUiOiJVU0VSIn0.hAsMDz0bUx11VrUjibij7V1MLYCamuniTE3f6Y2RtalXZX" +
                "_95cl4wgqXtHIEMATisG40iOY7tzmABuWeNzpj-Q";
        Role role = new Role(1L, "USER");
        RegisterUserDTO registerDto = new RegisterUserDTO(username, password);

        when(usersRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(hashedPassword);
        when(rolesRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtString);

        String generatedToken = usersService.register(registerDto);

        assertThat(generatedToken).isEqualTo(jwtString);
        verify(usersRepository).existsByUsername(username);
        verify(passwordEncoder).encode(password);
        verify(rolesRepository).findByName("USER");
        verify(usersRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void shouldLoginUserSuccessfully() {
        String username = "TestUsername";
        String rawPassword = "TestPassword";
        String hashedPassword = "$2a$10$zIkMNb0CO5WToEC77/SOs.QYMfUV066ZZQN9sMnChQgnGYmulzlvy";
        String jwtString = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJUZXN0TElicmFyeSIsImlhdCI6MTc1MTIzMTY0MSwiZXhwIjo" +
                "xNzgyNzY3NjQxLCJhdWQiOiJ3d3cuZXhhbXBsZS5jb20iLCJzdWIiOiJsaWJyYXJ5QGV4YW1wbGUuY29tIiwiR2l2ZW5OYW1lIjoi" +
                "Sm9obm55IiwiU3VybmFtZSI6IlJvY2tldCIsIlJvbGUiOiJVU0VSIn0.hAsMDz0bUx11VrUjibij7V1MLYCamuniTE3f6Y2RtalXZX" +
                "_95cl4wgqXtHIEMATisG40iOY7tzmABuWeNzpj-Q";

        LoginUserDTO loginUserDTO = new LoginUserDTO(username, rawPassword);
        User user = new User(1L, username, hashedPassword, List.of(new Role(1L, "USER")));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                hashedPassword,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        when(libraryUsersDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);
        when(usersRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(jwtString);

        String resultToken = usersService.login(loginUserDTO);

        assertThat(resultToken).isEqualTo(jwtString);
        verify(libraryUsersDetailsService).loadUserByUsername(username);
        verify(passwordEncoder).matches(rawPassword, hashedPassword);
        verify(usersRepository).findByUsername(username);
        verify(jwtService).generateToken(user);
    }

    @Test
    void shouldReturnAllUsers() {
        Role userRole = new Role(1L, "USER");
        Role adminRole = new Role(2L, "ADMIN");
        List<User> users = List.of(
                new User(1L, "TestUser", "TestPass", List.of(userRole)),
                new User(2L, "TestAdmin", "TestAmdinPass", List.of(adminRole))
        );

        when(usersRepository.findAll()).thenReturn(users);

        List<UserResponseDTO> usersDto = usersService.getAllUsers();

        for (int i = 0; i < usersDto.size(); i++) {
            assertThat(usersDto.get(i).getUsername()).isEqualTo(users.get(i).getUsername());
        }
        verify(usersRepository).findAll();
    }
}
