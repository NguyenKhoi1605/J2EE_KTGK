package com.clinic.appointment.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Patient Entity: Represents patients in the system
 * Has N-N relationship with Role
 */
@Entity
@Table(name = "patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // Will be hashed using BCryptPasswordEncoder

    @Column(nullable = false, unique = true)
    private String email;

    // Many-to-Many relationship with Role
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "patient_role",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // ============ Constructors ============
    
    /**
     * Default no-args constructor for JPA
     */
    public Patient() {
    }

    /**
     * Full-args constructor
     */
    public Patient(Long id, String username, String password, String email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    /**
     * Constructor without ID (for creation)
     */
    public Patient(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = new HashSet<>();
    }

    // ============ Getters and Setters ============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // ============ Builder Pattern ============

    public static PatientBuilder builder() {
        return new PatientBuilder();
    }

    public static class PatientBuilder {
        private Long id;
        private String username;
        private String password;
        private String email;
        private Set<Role> roles = new HashSet<>();

        public PatientBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PatientBuilder username(String username) {
            this.username = username;
            return this;
        }

        public PatientBuilder password(String password) {
            this.password = password;
            return this;
        }

        public PatientBuilder email(String email) {
            this.email = email;
            return this;
        }

        public PatientBuilder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public Patient build() {
            Patient patient = new Patient(this.username, this.password, this.email);
            if (this.id != null) {
                patient.id = this.id;
            }
            patient.roles = this.roles;
            return patient;
        }
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}


