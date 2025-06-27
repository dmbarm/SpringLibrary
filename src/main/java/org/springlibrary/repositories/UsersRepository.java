package org.springlibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springlibrary.entities.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);
}
