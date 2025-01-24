package com.attendance.BackendAttendanceRT.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.BackendAttendanceRT.DTO.LoginUserDto;
import com.attendance.BackendAttendanceRT.DTO.RegisterUserDto;
import com.attendance.BackendAttendanceRT.DTO.VerifyUserDto;
import com.attendance.BackendAttendanceRT.Model.User;
import com.attendance.BackendAttendanceRT.Response.LoginResponse;
import com.attendance.BackendAttendanceRT.Service.AuthenticationService;
import com.attendance.BackendAttendanceRT.Service.JwtService;

@RequestMapping({"/auth"})
@RestController
public class AuthenticationController {
   private final JwtService jwtService;
   private final AuthenticationService authenticationService;

   public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
      this.jwtService = jwtService;
      this.authenticationService = authenticationService;
   }

@PostMapping({"/signup"})
public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
    try {
        System.out.println("Request Body for register: " + 
                           registerUserDto.getEmail() + ", " + 
                           registerUserDto.getPassword() + ", " + 
                           registerUserDto.getUsername());
        User registeredUser = this.authenticationService.signup(registerUserDto);
        System.out.println("Request Body for registeredUser: " + 
                           registeredUser.getEmail() + ", " + 
                           registeredUser.getUsername());
        return ResponseEntity.ok(registeredUser);
    } catch (Exception e) {
        e.printStackTrace();  // You can replace this with a proper logger.
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

@PostMapping("/login")
public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
    try {
        User authenticatedUser = this.authenticationService.authenticate(loginUserDto);
        String jwtToken = this.jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, this.jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
}

//    @PostMapping({"/login"})
//    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
//       User authenticatedUser = this.authenticationService.authenticate(loginUserDto);
//       System.out.println("Login USer"+loginUserDto.getEmail()+loginUserDto.getPassword());
//       System.out.println("Login authenticatedUser"+authenticatedUser.getEmail()+authenticatedUser.getPassword());
//       String jwtToken = this.jwtService.generateToken(authenticatedUser);
//       LoginResponse loginResponse = new LoginResponse(jwtToken, this.jwtService.getExpirationTime());
//       return ResponseEntity.ok(loginResponse);
//    }

   @PostMapping({"/verify"})
   public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
      try {
         this.authenticationService.verifyUser(verifyUserDto);
         return ResponseEntity.ok("Account verified successfully");
      } catch (RuntimeException var3) {
         return ResponseEntity.badRequest().body(var3.getMessage());
      }
   }

   @PostMapping({"/resend"})
   public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
      try {
         this.authenticationService.resendVerificationCode(email);
         return ResponseEntity.ok("Verification code sent");
      } catch (RuntimeException var3) {
         return ResponseEntity.badRequest().body(var3.getMessage());
      }
   }

   
}

