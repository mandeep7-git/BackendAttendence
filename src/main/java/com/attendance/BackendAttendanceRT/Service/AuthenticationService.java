package com.attendance.BackendAttendanceRT.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.attendance.BackendAttendanceRT.DTO.LoginUserDto;
import com.attendance.BackendAttendanceRT.DTO.RegisterUserDto;
import com.attendance.BackendAttendanceRT.DTO.VerifyUserDto;
import com.attendance.BackendAttendanceRT.Model.Role;
import com.attendance.BackendAttendanceRT.Model.User;
import com.attendance.BackendAttendanceRT.Repo.UserRepository;

@Service
public class AuthenticationService {
private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
   private final AuthenticationManager authenticationManager;
   private final EmailService emailService;
   public static final String ROLE_USER = "ROLE_USER";

   public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, EmailService emailService) {
      this.authenticationManager = authenticationManager;
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
      this.emailService = emailService;
   }

   public User signup(RegisterUserDto registerUserDto) {
    // Validate the input
    validateRegisterUserDto(registerUserDto);

    try {
        // Encode password
        String encodedPassword = this.passwordEncoder.encode(registerUserDto.getPassword());

        // Create a new user
       // User user = createUser(registerUserDto, encodedPassword);
       //User user = new User(registerUserDto.getUsername(), registerUserDto.getEmail(), encodedPassword);
       User user = new User();
       user.setUsername(registerUserDto.getUsername());
       user.setEmail(registerUserDto.getEmail());
       user.setPassword(encodedPassword);

      // user.setRoles(Collections.singletonList(new SimpleGrantedAuthority(Role.ROLE_USER))); // Using the constant
      user.setRoles(List.of("ROLE_USER"));
      
       
        System.out.println("Again back in AUthserc=vice"+user.getEmail()+user.getUsername());
        // Generate and set verification details
        setupVerificationDetails(user);


        // Send verification email
        this.sendVerificationEmail(user);

        // Save and return the user
        return this.userRepository.save(user);

    } catch (Exception e) {
        // Handle errors and log them
        throw new RuntimeException("User registration failed. Please try again.", e);
    }
}

private void validateRegisterUserDto(RegisterUserDto registerUserDto) {
    System.out.println("Validate FUnction"+registerUserDto.getEmail()+registerUserDto.getUsername());
    if (registerUserDto.getEmail() == null || registerUserDto.getEmail().isEmpty()) {
        throw new IllegalArgumentException("Email address must not be null or empty");
    }
    if (registerUserDto.getUsername() == null || registerUserDto.getUsername().isEmpty()) {
        throw new IllegalArgumentException("Username must not be null or empty");
    }
    if (registerUserDto.getPassword() == null || registerUserDto.getPassword().isEmpty()) {
        throw new IllegalArgumentException("Password must not be null or empty");
    }
}

private User createUser(RegisterUserDto registerUserDto, String encodedPassword) {
    // Create and return the user
    System.out.println("after validate createUser"+registerUserDto.getEmail()+registerUserDto.getUsername()+registerUserDto.getPassword());
    return new User(registerUserDto.getUsername(), registerUserDto.getEmail(), encodedPassword);
}

private void setupVerificationDetails(User user) {
    user.setVerificationCode(this.generateVerificationCode());
    user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15L));
    user.setEnabled(false); // Initially disabled
}

public User authenticate(LoginUserDto input) {
    User user = userRepository.findByEmail(input.getEmail())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (!user.isEnabled()) {
        throw new RuntimeException("Account not verified. Please verify your account.");
    }

    try {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
        );
    } catch (Exception e) {
        throw new RuntimeException("Invalid credentials: " + e.getMessage());
    }

    return user;
}


//    public User authenticate(LoginUserDto input) {
//       User user = (User)this.userRepository.findByEmail(input.getEmail()).orElseThrow(() -> {
//          return new RuntimeException("User not found");
//       });
//       if (!user.isEnabled()) {
//          throw new RuntimeException("Account not verified. Please verify your account.");
//       } else {
//          this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
//          return user;
//       }
//    }

   public void verifyUser(VerifyUserDto input) {
      Optional<User> optionalUser = this.userRepository.findByEmail(input.getEmail());
      if (optionalUser.isPresent()) {
         User user = optionalUser.get();
         if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code has expired");
         } else if (user.getVerificationCode().equals(input.getVerificationCode())) {
            user.setEnabled(true);
            user.setVerificationCode((String)null);
            user.setVerificationCodeExpiresAt((LocalDateTime)null);
            this.userRepository.save(user);
         } else {
            throw new RuntimeException("Invalid verification code");
         }
      } else {
         throw new RuntimeException("User not found");
      }
   }

   public void resendVerificationCode(String email) {
      Optional<User> optionalUser = this.userRepository.findByEmail(email);
      if (optionalUser.isPresent()) {
         User user = (User)optionalUser.get();
         if (user.isEnabled()) {
            throw new RuntimeException("Account is already verified");
         } else {
            user.setVerificationCode(this.generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1L));
            this.sendVerificationEmail(user);
            this.userRepository.save(user);
         }
      } else {
         throw new RuntimeException("User not found");
      }
   }

   private void sendVerificationEmail(User user) {
      String subject = "Account Verification";
      String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
      String htmlMessage = "<html><body style=\"font-family: Arial, sans-serif;\"><div style=\"background-color: #f5f5f5; padding: 20px;\"><h2 style=\"color: #333;\">Welcome to our app!</h2><p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p><div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\"><h3 style=\"color: #333;\">Verification Code:</h3><p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p></div></div></body></html>";

      this.emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);

   }

   private String generateVerificationCode() {
      Random random = new Random();
      int code = random.nextInt(900000) + 100000;
      return String.valueOf(code);
   }
}

