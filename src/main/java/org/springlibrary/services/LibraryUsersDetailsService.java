package org.springlibrary.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springlibrary.entities.User;
import org.springlibrary.repositories.UsersRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryUsersDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    public LibraryUsersDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("error.user.notfound"));

        List<GrantedAuthority> grantedAuthorities = parseUserAuthorities(user);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    private List<GrantedAuthority> parseUserAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }
}
