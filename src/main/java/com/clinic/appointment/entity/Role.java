package com.clinic.appointment.entity;

import jakarta.persistence.*;

/**
 * Role Entity: Represents user roles in the system (ADMIN, PATIENT)
 */
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // ROLE_ADMIN, ROLE_PATIENT

    // ============ Constructors ============
    
    /**
     * Default no-args constructor for JPA
     */
    public Role() {
    }

    /**
     * Full-args constructor
     */
    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Constructor without ID (for creation)
     */
    public Role(String name) {
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
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

