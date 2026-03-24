package com.clinic.appointment.repository;

import com.clinic.appointment.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DoctorRepository: Repository for Doctor entity with pagination support
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    /**
     * Find all doctors with pagination
     * @param pageable pagination information
     * @return Page of doctors
     */
    Page<Doctor> findAll(Pageable pageable);

    /**
     * Search doctors by name (case-insensitive)
     * @param name the doctor's name to search
     * @return List of matching doctors
     */
    List<Doctor> findByNameContainingIgnoreCase(String name);

    /**
     * Search doctors by name with pagination
     * @param name the doctor's name to search
     * @param pageable pagination information
     * @return Page of matching doctors
     */
    Page<Doctor> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Search doctors by specialty
     * @param specialty the specialty to search
     * @return List of matching doctors
     */
    List<Doctor> findBySpecialtyContainingIgnoreCase(String specialty);
}
