package com.clinic.appointment.repository;

import com.clinic.appointment.entity.Appointment;
import com.clinic.appointment.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AppointmentRepository: Repository for Appointment entity
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    /**
     * Find all appointments for a specific patient
     * @param patient the patient object
     * @return List of appointments for the patient
     */
    List<Appointment> findByPatient(Patient patient);

    /**
     * Find appointments for a patient after a specific date
     * @param patient the patient object
     * @param appointmentDate the date to filter from
     * @return List of future appointments
     */
    List<Appointment> findByPatientAndAppointmentDateAfter(Patient patient, LocalDateTime appointmentDate);

    /**
     * Find appointments by doctor ID
     * @param doctorId the doctor's ID
     * @return List of appointments for the doctor
     */
    List<Appointment> findByDoctorId(Long doctorId);

    /**
     * Check if an appointment exists for a specific patient and date
     * @param patientId the patient's ID
     * @param appointmentDate the appointment date
     * @return true if appointment exists
     */
    boolean existsByPatientIdAndAppointmentDate(Long patientId, LocalDateTime appointmentDate);
}
