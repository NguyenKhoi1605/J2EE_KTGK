package com.clinic.appointment.controller;

import com.clinic.appointment.service.AppointmentService;
import com.clinic.appointment.service.DoctorService;
import com.clinic.appointment.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * HomeController: Handles home page and doctor listing
 */
@Controller
public class HomeController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private PatientService patientService;
    private static final int PAGE_SIZE = 5; // 5 doctors per page

    /**
     * Display home page with paginated doctor list
     */
    @GetMapping({"/", "/home"})
    public String home(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        var doctorPage = doctorService.findAllWithPagination(pageable);
        
        model.addAttribute("doctors", doctorPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", doctorPage.getTotalPages());
        model.addAttribute("totalElements", doctorPage.getTotalElements());

        return "home";
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
     * Handle access denied
     */
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
