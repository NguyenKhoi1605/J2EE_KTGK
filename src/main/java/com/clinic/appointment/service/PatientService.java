package com.clinic.appointment.service;

import com.clinic.appointment.entity.Patient;
import com.clinic.appointment.entity.Role;
import com.clinic.appointment.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * PatientService: Business logic for Patient operations
 * Handles user registration and patient management
 */
@Service
@Transactional
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Get all patients
     */
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    /**
     * Get a patient by ID
     */
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    /**
     * Get a patient by username
     */
    public Optional<Patient> findByUsername(String username) {
        return patientRepository.findByUsername(username);
    }

    /**
     * Get a patient by email
     */
    public Optional<Patient> findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    /**
     * Check if username exists
     */
    public boolean usernameExists(String username) {
        return patientRepository.existsByUsername(username);
    }

    /**
     * Check if email exists
     */
    public boolean emailExists(String email) {
        return patientRepository.existsByEmail(email);
    }

    /**
     * Register a new patient with default ROLE_PATIENT
     * Password is encoded using BCryptPasswordEncoder
     */
    public Patient registerPatient(String username, String password, String email) {
        // Validate that username and email don't already exist
        if (usernameExists(username)) {
            throw new RuntimeException("Username is already taken: " + username);
        }
        if (emailExists(email)) {
            throw new RuntimeException("Email is already registered: " + email);
        }

        // Create new patient with encoded password
        Patient patient = Patient.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();

        // Assign default ROLE_PATIENT role
        Role patientRole = roleService.findByName("ROLE_PATIENT")
                .orElseThrow(() -> new RuntimeException("ROLE_PATIENT not found"));
        
        Set<Role> roles = new HashSet<>();
        roles.add(patientRole);
        patient.setRoles(roles);

        return patientRepository.save(patient);
    }

    /**
     * Update patient information
     */
    public Patient updatePatient(Long id, String email) {
        return patientRepository.findById(id).map(patient -> {
            patient.setEmail(email);
            return patientRepository.save(patient);
        }).orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    /**
     * Change patient password
     */
    public void changePassword(Long patientId, String oldPassword, String newPassword) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        // Verify old password
        if (!passwordEncoder.matches(oldPassword, patient.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Update with new encoded password
        patient.setPassword(passwordEncoder.encode(newPassword));
        patientRepository.save(patient);
    }

    /**
     * Create an admin user with ROLE_ADMIN
     */
    public Patient createAdminUser(String username, String password, String email) {
        // Check if admin user already exists
        if (usernameExists(username)) {
            throw new RuntimeException("Username is already taken: " + username);
        }
        if (emailExists(email)) {
            throw new RuntimeException("Email is already registered: " + email);
        }

        // Create new admin patient with encoded password
        Patient patient = Patient.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();

        // Assign ROLE_ADMIN role
        Role adminRole = roleService.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        patient.setRoles(roles);

        return patientRepository.save(patient);
    }

    /**
     * Delete a patient
     */
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
