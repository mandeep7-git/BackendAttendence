package com.attendance.BackendAttendanceRT.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginUserDto {
    private String email;
   private String password;
}
