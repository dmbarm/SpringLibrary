package org.springlibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springlibrary.entities.Role;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String roleUser);
}
