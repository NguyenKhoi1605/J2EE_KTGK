package com.clinic.appointment.service;

import com.clinic.appointment.entity.Appointment;
import com.clinic.appointment.entity.Doctor;
import com.clinic.appointment.entity.Patient;
import com.clinic.appointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * AppointmentService: Business logic for Appointment operations
 */
@Service
@Transactional
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorService doctorService;

    /**
     * Get all appointments
     */
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    /**
     * Get an appointment by ID
     */
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    /**
     * Get all appointments for a specific patient
     */
    public List<Appointment> findAppointmentsByPatient(Patient patient) {
        return appointmentRepository.findByPatient(patient);
    }

    /**
     * Get future appointments for a specific patient
     */
    public List<Appointment> findFutureAppointmentsByPatient(Patient patient) {
        return appointmentRepository.findByPatientAndAppointmentDateAfter(patient, LocalDateTime.now());
    }

    /**
     * Get all appointments for a specific doctor
     */
    public List<Appointment> findAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    /**
     * Book a new appointment
     */
    public Appointment bookAppointment(Long patientId, Long doctorId, LocalDateTime appointmentDate) {
        // Validate that appointment date is in the future
        if (appointmentDate.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Appointment date must be in the future");
        }

        // Check if patient already has an appointment at this exact time
        if (appointmentRepository.existsByPatientIdAndAppointmentDate(patientId, appointmentDate)) {
            throw new RuntimeException("Patient already has an appointment at this date/time");
        }

        // Get patient and doctor
        Patient patient = patientService.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
        
        Doctor doctor = doctorService.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        // Create and save appointment
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(appointmentDate)
                .build();

        return appointmentRepository.save(appointment);
    }

    /**
     * Cancel an appointment
     */
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        // Check if appointment is in the future
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot cancel past appointments");
        }

        appointmentRepository.deleteById(appointmentId);
    }

    /**
     * Reschedule an appointment
     */
    public Appointment rescheduleAppointment(Long appointmentId, LocalDateTime newDate) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        // Validate new date
        if (newDate.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("New appointment date must be in the future");
        }

        // Check if patient already has an appointment at new time
        if (appointmentRepository.existsByPatientIdAndAppointmentDate(appointment.getPatient().getId(), newDate)) {
            throw new RuntimeException("Patient already has an appointment at this new date/time");
        }

        appointment.setAppointmentDate(newDate);
        return appointmentRepository.save(appointment);
    }

    /**
     * Delete an appointment
     */
    public void deleteAppointment(Long appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }
}
