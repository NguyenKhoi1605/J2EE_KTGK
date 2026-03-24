package com.clinic.appointment.entity;

import jakarta.persistence.*;

/**
 * Doctor Entity: Represents doctors in the clinic
 * Has N-1 relationship with Department
 */
@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "image_url")
    private String image; // URL or path to doctor's image

    @Column(nullable = false)
    private String specialty; // e.g., "Cardiology", "Pediatrics"

    // Many-to-One relationship with Department
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // ============ Constructors ============
    
    /**
     * Default no-args constructor for JPA
     */
    public Doctor() {
    }

    /**
     * Full-args constructor
     */
    public Doctor(Long id, String name, String image, String specialty, Department department) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.specialty = specialty;
        this.department = department;
    }

    /**
     * Constructor without ID (for creation)
     */
    public Doctor(String name, String image, String specialty, Department department) {
        this.name = name;
        this.image = image;
        this.specialty = specialty;
        this.department = department;
    }

    // ============ Getters and Setters ============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    // ============ Builder Pattern ============

    public static DoctorBuilder builder() {
        return new DoctorBuilder();
    }

    public static class DoctorBuilder {
        private Long id;
        private String name;
        private String image;
        private String specialty;
        private Department department;

        public DoctorBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DoctorBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DoctorBuilder image(String image) {
            this.image = image;
            return this;
        }

        public DoctorBuilder specialty(String specialty) {
            this.specialty = specialty;
            return this;
        }

        public DoctorBuilder department(Department department) {
            this.department = department;
            return this;
        }

        public Doctor build() {
            Doctor doctor = new Doctor(this.name, this.image, this.specialty, this.department);
            if (this.id != null) {
                doctor.id = this.id;
            }
            return doctor;
        }
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialty='" + specialty + '\'' +
                '}';
    }
}


