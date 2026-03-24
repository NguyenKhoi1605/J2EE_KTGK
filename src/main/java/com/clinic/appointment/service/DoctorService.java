package com.clinic.appointment.service;

import com.clinic.appointment.entity.Doctor;
import com.clinic.appointment.entity.Department;
import com.clinic.appointment.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * DoctorService: Business logic for Doctor operations with pagination support
 */
@Service
@Transactional
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DepartmentService departmentService;
    
    @Value("${app.upload.dir:uploads/doctors}")
    private String uploadDir;

    /**
     * Get all doctors with pagination
     */
    public Page<Doctor> findAllWithPagination(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    /**
     * Get all doctors
     */
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    /**
     * Get a doctor by ID
     */
    public Optional<Doctor> findById(Long id) {
        return doctorRepository.findById(id);
    }

    /**
     * Search doctors by name
     */
    public List<Doctor> searchByName(String name) {
        return doctorRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Search doctors by name with pagination
     */
    public Page<Doctor> searchByNameWithPagination(String name, Pageable pageable) {
        return doctorRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    /**
     * Search doctors by specialty
     */
    public List<Doctor> searchBySpecialty(String specialty) {
        return doctorRepository.findBySpecialtyContainingIgnoreCase(specialty);
    }

    /**
     * Create a new doctor
     */
    public Doctor createDoctor(String name, String image, String specialty, Long departmentId) {
        Department department = departmentService.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
        
        Doctor doctor = Doctor.builder()
                .name(name)
                .image(image)
                .specialty(specialty)
                .department(department)
                .build();
        
        return doctorRepository.save(doctor);
    }

    /**
     * Update an existing doctor
     */
    public Doctor updateDoctor(Long id, String name, String image, String specialty, Long departmentId) {
        return doctorRepository.findById(id).map(doctor -> {
            doctor.setName(name);
            doctor.setImage(image);
            doctor.setSpecialty(specialty);
            
            Department department = departmentService.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
            doctor.setDepartment(department);
            
            return doctorRepository.save(doctor);
        }).orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
    }

    /**
     * Create a new doctor with image
     */
    public Doctor createDoctorWithImage(String name, String imagePath, String specialty, Long departmentId) {
        Department department = departmentService.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
        
        Doctor doctor = Doctor.builder()
                .name(name)
                .image(imagePath)
                .specialty(specialty)
                .department(department)
                .build();
        
        return doctorRepository.save(doctor);
    }

    /**
     * Upload doctor image file
     * Returns the relative path to access the image via /uploads/{filename}
     */
    public String uploadDoctorImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        try {
            // Create uploads directory if not exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename to avoid conflicts
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // Save file
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.write(filePath, file.getBytes());

            // Return path to access via /uploads/{filename}
            return "/uploads/" + uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    /**
     * Delete a doctor
     */
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    /**
     * Initialize sample doctors
     */
    public void initializeSampleDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        if (!doctors.isEmpty()) {
            return; // Sample data already exists
        }

        Optional<Department> cardiology = departmentService.findByName("Cardiology");
        Optional<Department> pediatrics = departmentService.findByName("Pediatrics");
        Optional<Department> dermatology = departmentService.findByName("Dermatology");

        if (cardiology.isPresent()) {
            createDoctor("Dr. John Smith", "/uploads/5a93ee62-b15f-4d77-a344-bcb0cd6d6503.jpeg", "Cardiologist", cardiology.get().getId());
            createDoctor("Dr. Sarah Johnson", "/uploads/939c018c-81b8-4c9d-baae-398c439dba8b.jpeg", "Cardiologist", cardiology.get().getId());
        }

        if (pediatrics.isPresent()) {
            createDoctor("Dr. Mike Chen", "/uploads/3ee642ff-b61f-4933-b5eb-e591c95c7cb8.jpeg", "Pediatrician", pediatrics.get().getId());
        }

        if (dermatology.isPresent()) {
            createDoctor("Dr. Emma Wilson", "/uploads/5a93ee62-b15f-4d77-a344-bcb0cd6d6503.jpeg", "Dermatologist", dermatology.get().getId());
        }
    }
}
