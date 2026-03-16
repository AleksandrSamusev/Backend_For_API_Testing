package dev.practice.shopapp.repositories;

import dev.practice.shopapp.enums.UserRole;
import dev.practice.shopapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);
}
