package com.attendance.BackendAttendanceRT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.attendance.BackendAttendanceRT.Model.User;
import com.attendance.BackendAttendanceRT.Repo.UserRepository;

@SpringBootApplication
public class BackendAttendanceRtApplication {

	    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BackendAttendanceRtApplication.class, args);
	}

//   // This method runs after the application starts and seeds the admin user if not already present
//     @Bean
//     public CommandLineRunner seedAdminUser() {
//         return args -> {
//             // Check if the admin user already exists
//             if (userRepository.existsByEmail("admin@example.com")) {
//                 return; // If admin exists, do nothing
//             }

//             // Create and save the admin user
//             User admin = new User();
//             admin.setEmail("admin@example.com");
//             admin.setUsername("Admin User");
//             admin.setPassword(passwordEncoder.encode("admin123")); // Ensure a secure password is used
//             admin.setRoles(List.of("ROLE_ADMIN")); // You can assign both admin and user roles
//             userRepository.save(admin); // Save the user to the database

//             System.out.println("Admin user seeded into the database.");
//         };
//     }
}
