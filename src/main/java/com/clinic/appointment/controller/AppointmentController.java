package com.clinic.appointment.controller;

import com.clinic.appointment.service.AppointmentService;
import com.clinic.appointment.service.DoctorService;
import com.clinic.appointment.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * AppointmentController: Handles appointment-related endpoints
 * Only accessible to patients with ROLE_PATIENT
 * Allows booking, viewing, and managing appointments
 */
@Controller
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorService doctorService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /**
     * Display appointment booking form
     */
    @GetMapping("/book")
    public String showBookingForm(@RequestParam(required = false) Long doctorId, Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        
        if (doctorId != null) {
            var doctor = doctorService.findById(doctorId);
            doctor.ifPresent(d -> model.addAttribute("selectedDoctor", d));
        }
        
        return "appointment/book";
    }

    /**
     * Handle appointment booking
     */
    @PostMapping("/book")
    public String bookAppointment(@RequestParam Long doctorId,
                                 @RequestParam String appointmentDateTime,
                                 Model model) {
        try {
            // Get current logged-in patient
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            var patient = patientService.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));

            // Parse appointment date time
            LocalDateTime appointmentDate = LocalDateTime.parse(appointmentDateTime, DATE_FORMATTER);

            // Book appointment
            appointmentService.bookAppointment(patient.getId(), doctorId, appointmentDate);
            
            return "redirect:/my-appointments?success=true";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("doctors", doctorService.findAll());
            return "appointment/book";
        }
    }

    /**
     * Display patient's appointments
     */
    @GetMapping("/my-appointments")
    public String myAppointments(Model model) {
        try {
            // Get current logged-in patient
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            var patient = patientService.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));

            // Get patient's appointments
            var appointments = appointmentService.findFutureAppointmentsByPatient(patient);
            
            model.addAttribute("appointments", appointments);
            model.addAttribute("patientName", patient.getUsername());
            
            return "appointment/my-appointments";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "appointment/my-appointments";
        }
    }

    /**
     * Display all appointments (admin view)
     */
    @GetMapping("/list")
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.findAll());
        return "appointment/list";
    }

    /**
     * Handle appointment cancellation
     */
    @GetMapping("/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id) {
        try {
            appointmentService.cancelAppointment(id);
            return "redirect:/my-appointments?success=true";
        } catch (RuntimeException e) {
            return "redirect:/my-appointments?error=true";
        }
    }

    /**
     * Display form to reschedule appointment
     */
    @GetMapping("/reschedule/{id}")
    public String showRescheduleForm(@PathVariable Long id, Model model) {
        var appointment = appointmentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        
        model.addAttribute("appointment", appointment);
        model.addAttribute("doctors", doctorService.findAll());
        
        return "appointment/reschedule";
    }

    /**
     * Handle appointment rescheduling
     */
    @PostMapping("/reschedule/{id}")
    public String rescheduleAppointment(@PathVariable Long id,
                                       @RequestParam String appointmentDateTime,
                                       Model model) {
        try {
            // Parse new appointment date time
            LocalDateTime newDate = LocalDateTime.parse(appointmentDateTime, DATE_FORMATTER);

            // Reschedule appointment
            appointmentService.rescheduleAppointment(id, newDate);
            
            return "redirect:/my-appointments?success=true";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("doctors", doctorService.findAll());
            return "appointment/reschedule";
        }
    }
}
