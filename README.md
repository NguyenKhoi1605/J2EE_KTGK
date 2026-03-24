# Clinic Appointment System

A professional healthcare clinic appointment management application built with Spring Boot 3.x, PostgreSQL, and Thymeleaf.

## Project Overview

The Clinic Appointment System is a web-based application that allows patients to:
- Browse available doctors with detailed information
- Book appointments with doctors
- Manage their appointments (view, reschedule, cancel)
- Search for doctors by name or specialty

Administrators can:
- Manage doctor information (add, edit, delete)
- Monitor all appointments in the system
- Manage departments and roles

## Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.x** - Web framework
- **Spring Data JPA** - ORM and database access
- **Spring Security** - Authentication and authorization
- **Spring MVC** - Web layer
- **Validation** - Input validation

### Database
- **PostgreSQL** - Relational database

### Frontend
- **Thymeleaf** - Server-side template engine
- **Bootstrap 5** - CSS framework
- **HTML5** - Markup

### Build & Tools
- **Maven** - Build and dependency management
- **Lombok** - Code generation library

## Project Structure

```
clinic-appointment/
├── src/
│   ├── main/
│   │   ├── java/com/clinic/appointment/
│   │   │   ├── entity/          # JPA entities
│   │   │   │   ├── Appointment.java
│   │   │   │   ├── Department.java
│   │   │   │   ├── Doctor.java
│   │   │   │   ├── Patient.java
│   │   │   │   └── Role.java
│   │   │   ├── repository/      # Spring Data repositories
│   │   │   │   ├── AppointmentRepository.java
│   │   │   │   ├── DepartmentRepository.java
│   │   │   │   ├── DoctorRepository.java
│   │   │   │   ├── PatientRepository.java
│   │   │   │   └── RoleRepository.java
│   │   │   ├── service/         # Business logic
│   │   │   │   ├── AppointmentService.java
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   ├── DepartmentService.java
│   │   │   │   ├── DoctorService.java
│   │   │   │   ├── PatientService.java
│   │   │   │   └── RoleService.java
│   │   │   ├── controller/      # REST/MVC controllers
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── AppointmentController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── DoctorController.java
│   │   │   │   └── HomeController.java
│   │   │   ├── config/          # Configuration classes
│   │   │   │   ├── DataInitializer.java
│   │   │   │   └── SecurityConfig.java
│   │   │   └── ClinicAppointmentApplication.java
│   │   └── resources/
│   │       ├── application.properties  # Configuration file
│   │       ├── templates/              # Thymeleaf templates
│   │       └── static/                 # Static files (CSS, JS, images)
│   └── test/
│       └── java/
├── pom.xml                      # Maven configuration
└── README.md                    # This file
```

## Database Schema

### Entities and Relationships

```
Department (1) ←─→ (N) Doctor
Role (M) ←─→ (N) Patient
Doctor (1) ←─→ (N) Appointment ←─→ (1) Patient
```

### Tables

- **role**: User roles (ROLE_ADMIN, ROLE_PATIENT)
- **department**: Medical departments
- **doctor**: Doctor information with department reference
- **patient**: Patient/User account information
- **patient_role**: Many-to-many relationship between Patient and Role
- **appointment**: Appointment bookings with patient and doctor references

## Installation & Setup

### Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher

### Step 1: Create PostgreSQL Database

```sql
CREATE DATABASE clinic_db;
CREATE USER clinic_user WITH PASSWORD 'clinic_password';
ALTER ROLE clinic_user WITH CREATEDB;
GRANT ALL PRIVILEGES ON DATABASE clinic_db TO clinic_user;
```

### Step 2: Configure Database Connection

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/clinic_db
spring.datasource.username=clinic_user
spring.datasource.password=clinic_password
```

### Step 3: Build the Project

```bash
cd clinic-appointment
mvn clean install
```

### Step 4: Run the Application

```bash
mvn spring-boot:run
```

The application will start at: **http://localhost:8080**

## Features & Functionality

### 1. Home Page & Doctor Listing
- Display paginated list of all doctors (5 per page)
- Show doctor name, specialty, department, and image
- Navigation to doctor details and booking page

### 2. Doctor Management
- Search doctors by name (case-insensitive)
- View detailed information about each doctor
- Filter by specialty and department

### 3. User Authentication
- Public registration page
- Password encryption using BCryptPasswordEncoder
- Login/Logout functionality
- Role-based access control

### 4. Appointment Booking
- Patients can book appointments with selected doctor
- Choose appointment date and time
- View upcoming appointments
- Reschedule or cancel appointments

### 5. Admin Functions (ROLE_ADMIN)
- Admin Dashboard
- Add new doctors
- Edit existing doctor information
- Delete doctors
- View all appointments in the system

## Security & Authorization

### Access Control

| Endpoint | Public | Patient | Admin |
|----------|--------|---------|-------|
| `/` (Home) | ✓ | ✓ | ✓ |
| `/doctors/**` | ✓ | ✓ | ✓ |
| `/register` | ✓ | - | - |
| `/login` | ✓ | - | - |
| `/my-appointments` | - | ✓ | - |
| `/appointment/**` | - | ✓ | - |
| `/admin/**` | - | - | ✓ |

### Password Security
- Passwords are hashed using BCryptPasswordEncoder
- Minimum 6 characters required
- Password confirmation on registration

## API Endpoints

### Authentication
- `GET /login` - Login form
- `POST /login` - Process login
- `GET /register` - Registration form
- `POST /register` - Process registration
- `GET /logout` - Logout (redirects to home)

### Home & Doctors
- `GET /` or `/home` - Home page with doctor list (paginated)
- `GET /doctors` - List all doctors (paginated)
- `GET /doctors/search` - Search doctors by keyword
- `GET /doctors/{id}` - View doctor details

### Appointments (Requires Authentication - ROLE_PATIENT)
- `GET /appointment/book` - Appointment booking form
- `POST /appointment/book` - Create appointment
- `GET /my-appointments` - View patient's appointments
- `GET /appointment/reschedule/{id}` - Reschedule form
- `POST /appointment/reschedule/{id}` - Update appointment
- `GET /appointment/cancel/{id}` - Cancel appointment
- `GET /appointment/list` - Admin view of all appointments

### Admin Functions (Requires Authentication - ROLE_ADMIN)
- `GET /admin/dashboard` - Admin dashboard
- `GET /admin/doctors` - List doctors (admin view)
- `GET /admin/doctors/create` - Create doctor form
- `POST /admin/doctors/create` - Process doctor creation
- `GET /admin/doctors/edit/{id}` - Edit doctor form
- `POST /admin/doctors/edit/{id}` - Update doctor
- `GET /admin/doctors/delete/{id}` - Delete doctor

## Default Test Credentials

After first run, the system initializes with:

**Admin Account** (Must be created manually for first setup)
- Username: admin
- Password: admin123
- Role: ROLE_ADMIN

**Sample Doctors**: 4 doctors across 3 departments are created automatically

## Usage Examples

### Register as Patient
1. Go to `http://localhost:8080/register`
2. Enter username, email, and password
3. Click "Create Account"
4. Go to login page and sign in

### Book Appointment
1. Login as patient
2. Click "Book Appointment" in navigation
3. Select doctor and appointment date/time
4. Click "Confirm Booking"
5. View appointment in "My Appointments"

### Manage Doctors (Admin)
1. Login as admin
2. Go to "Admin Dashboard"
3. Click "Manage Doctors"
4. Add, edit, or delete doctors as needed

## Configuration

### Application Properties

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/clinic_db
spring.datasource.username=postgres
spring.datasource.password=postgres

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Thymeleaf
spring.thymeleaf.cache=false
```

## Error Handling

- **403 Access Denied** - User lacks required role/permission
- **404 Not Found** - Resource not found
- **400 Bad Request** - Invalid input or validation failure
- **500 Internal Server Error** - Unexpected server error

## Development Notes

### Code Quality
- Clean Code principles applied throughout
- Meaningful comments on important sections
- Comprehensive service layer logic
- Proper exception handling

### Performance Considerations
- Pagination implemented (5 doctors per page)
- Lazy loading for relationships
- Indexed database queries using JPA
- Caching disabled in development (can be enabled for production)

### Future Enhancements
- Email notification system for appointments
- SMS reminders for patients
- Doctor availability scheduling
- Rating and review system for doctors
- Patient medical history records
- Export appointments to PDF/Excel
- Multi-language support
- Dashboard analytics and statistics

## Troubleshooting

### Database Connection Issues
```
Error: The connection attempt failed
Solution: Ensure PostgreSQL is running and credentials are correct
```

### Port Already in Use
```
Error: Web server failed to start.
Solution: Change port in application.properties (server.port=8081)
```

### Login Issues
```
Error: Invalid username or password
Solution: Ensure user exists and password is correct. Check case sensitivity.
```

## Support & Contact

For issues or questions, contact the development team at: dev@clinic.com

## License

This project is proprietary software. All rights reserved.

## Version History

**v1.0.0** (2026-03-24)
- Initial release
- Core functionality implemented
- Database design complete
- UI/UX with Bootstrap 5

---

**Last Updated**: March 24, 2026

Happy coding! 🏥
