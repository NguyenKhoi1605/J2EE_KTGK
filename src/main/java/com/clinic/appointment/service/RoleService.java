package com.clinic.appointment.service;

import com.clinic.appointment.entity.Role;
import com.clinic.appointment.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * RoleService: Business logic for Role operations
 */
@Service
@Transactional
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Get a role by ID
     */
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    /**
     * Get a role by name
     */
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    /**
     * Get all roles
     */
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    /**
     * Create a new role
     */
    public Role createRole(String name) {
        Role role = new Role(name);
        return roleRepository.save(role);
    }

    /**
     * Initialize default roles if they don't exist
     */
    public void initializeDefaultRoles() {
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
        if (roleRepository.findByName("ROLE_PATIENT").isEmpty()) {
            roleRepository.save(new Role("ROLE_PATIENT"));
        }
    }
}
