package com.clinic.appointment.service;

import com.clinic.appointment.entity.Patient;
import com.clinic.appointment.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * CustomUserDetailsService: Implements UserDetailsService for Spring Security
 * Loads user details from the database
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private PatientRepository patientRepository;

    /**
     * Load user by username for Spring Security authentication
     * @param username the username to load
     * @return UserDetails containing user information and authorities
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Patient patient = patientRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return User.builder()
                .username(patient.getUsername())
                .password(patient.getPassword())
                .authorities(patient.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}

