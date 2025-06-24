package org.springlibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springlibrary.entities.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
