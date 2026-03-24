package com.clinic.appointment.entity;

import jakarta.persistence.*;

/**
 * Department Entity: Represents medical departments
 */
@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., Cardiology, Pediatrics, etc.

    // ============ Constructors ============
    
    /**
     * Default no-args constructor for JPA
     */
    public Department() {
    }

    /**
     * Full-args constructor
     */
    public Department(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Constructor without ID (for creation)
     */
    public Department(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

