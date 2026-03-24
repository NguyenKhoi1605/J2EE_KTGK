package com.clinic.appointment.controller;

import com.clinic.appointment.service.DepartmentService;
import com.clinic.appointment.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * AdminController: Handles admin endpoints for managing doctors
 * Only accessible to users with ROLE_ADMIN
 * Provides CRUD operations for doctors
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private DepartmentService departmentService;

    /**
     * Display admin dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    /**
     * Display list of doctors (for admin)
     */
    @GetMapping("/doctors")
    public String listDoctors(Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        return "admin/doctors/list";
    }

    /**
     * Display form to create new doctor
     */
    @GetMapping("/doctors/create")
    public String showCreateForm(Model model) {
        model.addAttribute("departments", departmentService.findAll());
        return "admin/doctors/form";
    }

    /**
     * Handle doctor creation
     */
    @PostMapping("/doctors/create")
    public String createDoctor(@RequestParam String name,
                              @RequestParam(required = false) MultipartFile image,
                              @RequestParam String specialty,
                              @RequestParam Long departmentId,
                              Model model) {
        try {
            String imagePath = null;
            if (image != null && !image.isEmpty()) {
                imagePath = doctorService.uploadDoctorImage(image);
            }
            doctorService.createDoctorWithImage(name, imagePath, specialty, departmentId);
            return "redirect:/admin/doctors?success=true";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("departments", departmentService.findAll());
            return "admin/doctors/form";
        }
    }

    /**
     * Display form to edit doctor
     */
    @GetMapping("/doctors/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var doctor = doctorService.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        
        model.addAttribute("doctor", doctor);
        model.addAttribute("departments", departmentService.findAll());
        return "admin/doctors/form";
    }

    /**
     * Handle doctor update
     */
    @PostMapping("/doctors/edit/{id}")
    public String updateDoctor(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam(required = false) MultipartFile image,
                              @RequestParam String specialty,
                              @RequestParam Long departmentId,
                              Model model) {
        try {
            var doctor = doctorService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            
            String imagePath = doctor.getImage();
            if (image != null && !image.isEmpty()) {
                imagePath = doctorService.uploadDoctorImage(image);
            }
            
            doctorService.updateDoctor(id, name, imagePath, specialty, departmentId);
            return "redirect:/admin/doctors?success=true";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("departments", departmentService.findAll());
            return "admin/doctors/form";
        }
    }

    /**
     * Handle doctor deletion
     */
    @GetMapping("/doctors/delete/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return "redirect:/admin/doctors?success=true";
        } catch (RuntimeException e) {
            return "redirect:/admin/doctors?error=true";
        }
    }
}
