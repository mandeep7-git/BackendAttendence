package com.attendance.BackendAttendanceRT.Service;


import com.attendance.BackendAttendanceRT.Model.User;
import com.attendance.BackendAttendanceRT.Repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
       private final UserRepository userRepository;

   public UserService(UserRepository userRepository, EmailService emailService) {
      this.userRepository = userRepository;
   }

   public List<User> getAllUsers() {
    List<User> users = new ArrayList<>();
    Iterable<User> userIterable = userRepository.findAll();

    // Adding all users from the repository to the list
    userIterable.forEach(users::add);

    // Print the size or details of users for debugging
    System.out.println("USER:::" + users.size() + " users retrieved."); // Printing the size of the list

    return users;
}

}