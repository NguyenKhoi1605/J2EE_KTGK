package com.clinic.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClinicAppointmentApplication: Main entry point for the Spring Boot application
 * This is the Clinic Appointment Management System
 */
@SpringBootApplication
public class ClinicAppointmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClinicAppointmentApplication.class, args);
        System.out.println("========================================");
        System.out.println("Clinic Appointment System Started");
        System.out.println("Application URL: http://localhost:8080");
        System.out.println("========================================");
    }
}
