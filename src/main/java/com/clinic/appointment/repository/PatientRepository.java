package com.clinic.appointment.repository;

import com.clinic.appointment.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * PatientRepository: Repository for Patient entity
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    /**
     * Find a patient by username
     * @param username the patient's username
     * @return Optional containing the patient if found
     */
    Optional<Patient> findByUsername(String username);

    /**
     * Find a patient by email
     * @param email the patient's email
     * @return Optional containing the patient if found
     */
    Optional<Patient> findByEmail(String email);

    /**
     * Check if a username exists
     * @param username the username to check
     * @return true if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if an email exists
     * @param email the email to check
     * @return true if email exists
     */
    boolean existsByEmail(String email);
}
