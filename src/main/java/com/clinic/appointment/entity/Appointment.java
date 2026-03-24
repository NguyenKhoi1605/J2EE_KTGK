package com.clinic.appointment.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Appointment Entity: Represents doctor appointments booked by patients
 * Has N-1 relationships with both Patient and Doctor
 */
@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-One relationship with Patient
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Many-to-One relationship with Doctor
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    // ============ Constructors ============
    
    /**
     * Default no-args constructor for JPA
     */
    public Appointment() {
    }

    /**
     * Full-args constructor
     */
    public Appointment(Long id, Patient patient, Doctor doctor, LocalDateTime appointmentDate) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
    }

    /**
     * Constructor without ID (for creation)
     */
    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentDate) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
    }

    // ============ Getters and Setters ============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    // ============ Builder Pattern ============

    public static AppointmentBuilder builder() {
        return new AppointmentBuilder();
    }

    public static class AppointmentBuilder {
        private Long id;
        private Patient patient;
        private Doctor doctor;
        private LocalDateTime appointmentDate;

        public AppointmentBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AppointmentBuilder patient(Patient patient) {
            this.patient = patient;
            return this;
        }

        public AppointmentBuilder doctor(Doctor doctor) {
            this.doctor = doctor;
            return this;
        }

        public AppointmentBuilder appointmentDate(LocalDateTime appointmentDate) {
            this.appointmentDate = appointmentDate;
            return this;
        }

        public Appointment build() {
            Appointment appointment = new Appointment(this.patient, this.doctor, this.appointmentDate);
            if (this.id != null) {
                appointment.id = this.id;
            }
            return appointment;
        }
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", appointmentDate=" + appointmentDate +
                '}';
    }
}
