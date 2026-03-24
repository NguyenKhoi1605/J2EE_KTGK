package com.clinic.appointment.repository;

import com.clinic.appointment.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * RoleRepository: Repository for Role entity
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Find a role by name
     * @param name the role name (e.g., "ROLE_ADMIN", "ROLE_PATIENT")
     * @return Optional containing the role if found
     */
    Optional<Role> findByName(String name);
}
