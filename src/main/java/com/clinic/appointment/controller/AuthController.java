package com.clinic.appointment.controller;

import com.clinic.appointment.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * AuthController: Handles authentication-related endpoints
 * Login, Register, Logout
 */
@Controller
public class AuthController {
    @Autowired
    private PatientService patientService;

    /**
     * Display login form
     */
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
        return "auth/login";
    }

    /**
     * Display registration form
     */
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "auth/register";
    }

    /**
     * Handle registration form submission
     */
    @PostMapping("/register")
    public String registerPatient(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 @RequestParam String email,
                                 Model model) {
        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match");
            return "auth/register";
        }

        // Validate password length
        if (password.length() < 6) {
            model.addAttribute("errorMessage", "Password must be at least 6 characters long");
            return "auth/register";
        }

        try {
            // Attempt to register patient
            patientService.registerPatient(username, password, email);
            model.addAttribute("successMessage", "Registration successful! Please login.");
            return "auth/register";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "auth/register";
        }
    }

    /**
     * Logout is handled by Spring Security
     * This mapping is for display purposes if needed
     */
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/home";
    }
}
