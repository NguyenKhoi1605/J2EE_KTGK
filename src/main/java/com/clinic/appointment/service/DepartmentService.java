package com.clinic.appointment.service;

import com.clinic.appointment.entity.Department;
import com.clinic.appointment.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * DepartmentService: Business logic for Department operations
 */
@Service
@Transactional
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * Get all departments
     */
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    /**
     * Get a department by ID
     */
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    /**
     * Get a department by name
     */
    public Optional<Department> findByName(String name) {
        return departmentRepository.findByName(name);
    }

    /**
     * Create a new department
     */
    public Department createDepartment(String name) {
        Department department = new Department(name);
        return departmentRepository.save(department);
    }

    /**
     * Update an existing department
     */
    public Department updateDepartment(Long id, String name) {
        return departmentRepository.findById(id).map(dep -> {
            dep.setName(name);
            return departmentRepository.save(dep);
        }).orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    /**
     * Delete a department
     */
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    /**
     * Initialize sample departments
     */
    public void initializeSampleDepartments() {
        if (departmentRepository.findByName("Cardiology").isEmpty()) {
            createDepartment("Cardiology");
        }
        if (departmentRepository.findByName("Pediatrics").isEmpty()) {
            createDepartment("Pediatrics");
        }
        if (departmentRepository.findByName("Dermatology").isEmpty()) {
            createDepartment("Dermatology");
        }
        if (departmentRepository.findByName("Orthopedics").isEmpty()) {
            createDepartment("Orthopedics");
        }
    }
}
