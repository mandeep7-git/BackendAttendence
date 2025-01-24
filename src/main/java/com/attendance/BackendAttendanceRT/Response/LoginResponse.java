package com.attendance.BackendAttendanceRT.Response;


public class LoginResponse {
   private String token;
   private long expiresIn;

   public LoginResponse(String token, long expiresIn) {
      this.token = token;
      this.expiresIn = expiresIn;
   }

   public String getToken() {
      return this.token;
   }

   public long getExpiresIn() {
      return this.expiresIn;
   }

   public void setToken(final String token) {
      this.token = token;
   }

   public void setExpiresIn(final long expiresIn) {
      this.expiresIn = expiresIn;
   }
}
