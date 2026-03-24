package com.clinic.appointment.controller;

import com.clinic.appointment.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * DoctorController: Handles doctor-related endpoints
 * Provides doctor listing, search, and detail view
 */
@Controller
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    private static final int PAGE_SIZE = 5;

    /**
     * Display all doctors with pagination
     */
    @GetMapping
    public String listDoctors(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        var doctorPage = doctorService.findAllWithPagination(pageable);
        
        model.addAttribute("doctors", doctorPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", doctorPage.getTotalPages());

        return "doctors/list";
    }

    /**
     * Search doctors by name
     */
    @GetMapping("/search")
    public String searchDoctors(@RequestParam String keyword, 
                               @RequestParam(defaultValue = "0") int page, 
                               Model model) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        var doctorPage = doctorService.searchByNameWithPagination(keyword, pageable);
        
        model.addAttribute("doctors", doctorPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", doctorPage.getTotalPages());

        return "doctors/search-results";
    }

    /**
     * Display doctor details
     */
    @GetMapping("/{id}")
    public String doctorDetail(@PathVariable Long id, Model model) {
        var doctor = doctorService.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        
        model.addAttribute("doctor", doctor);
        return "doctors/detail";
    }
}
