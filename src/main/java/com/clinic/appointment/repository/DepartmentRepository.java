package com.clinic.appointment.repository;

import com.clinic.appointment.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DepartmentRepository: Repository for Department entity
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    /**
     * Find a department by name
     * @param name the department name
     * @return Optional containing the department if found
     */
    Optional<Department> findByName(String name);
}
