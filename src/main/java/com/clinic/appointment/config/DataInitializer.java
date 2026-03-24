package com.clinic.appointment.config;

import com.clinic.appointment.service.DepartmentService;
import com.clinic.appointment.service.DoctorService;
import com.clinic.appointment.service.PatientService;
import com.clinic.appointment.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DataInitializer: Initialize default data on application startup
 * Creates default roles, departments, sample doctors, and admin user
 */
@Configuration
public class DataInitializer {
    @Autowired
    private RoleService roleService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;

    /**
     * Initialize application data
     */
    @Bean
    public ApplicationRunner initializeData() {
        return args -> {
            // Initialize default roles
            roleService.initializeDefaultRoles();
            System.out.println("✓ Default roles initialized");

            // Initialize sample departments
            departmentService.initializeSampleDepartments();
            System.out.println("✓ Sample departments initialized");

            // Initialize sample doctors
            doctorService.initializeSampleDoctors();
            System.out.println("✓ Sample doctors initialized");

            // Initialize admin user
            initializeAdminUser();
            System.out.println("✓ Admin user initialized");

            System.out.println("✓ Database initialization completed successfully!");
        };
    }

    private void initializeAdminUser() {
        // Check if admin user already exists
        if (patientService.findByUsername("DKHOI").isPresent()) {
            System.out.println("  → Admin user 'DKHOI' already exists, skipping creation");
            return;
        }

        try {
            patientService.createAdminUser("DKHOI", "123456", "dkhoi@clinic.com");
            System.out.println("  → Admin user created successfully!");
            System.out.println("    Username: DKHOI");
            System.out.println("    Password: 123456");
            System.out.println("    Email: dkhoi@clinic.com");
        } catch (Exception e) {
            System.out.println("  ⚠ Admin user creation skipped: " + e.getMessage());
        }
    }
}
