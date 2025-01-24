package com.attendance.BackendAttendanceRT.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.attendance.BackendAttendanceRT.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Custom method to find a user by email
    Optional<User> findByEmail(String email);

    // Check if a user with the given email exists
    boolean existsByEmail(String email);
}
