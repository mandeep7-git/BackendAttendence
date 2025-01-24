package com.attendance.BackendAttendanceRT.Model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
 @Setter
 @Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usersReg")
public class User implements UserDetails {

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String username;

    private String password;

     @Column(
      name = "verification_code"
   )
   private String verificationCode;
   @Column(
      name = "verification_expiration"
   )
   private LocalDateTime verificationCodeExpiresAt;
   private boolean enabled;

// @ElementCollection(fetch = FetchType.EAGER)
//     @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
//     @Column(name = "role")
    private List<String> roles;

@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));

}

public boolean isAccountNonExpired() {
    return true;
 }

 public boolean isAccountNonLocked() {
    return true;
 }

 public boolean isCredentialsNonExpired() {
    return true;
 }

 public boolean isEnabled() {
    return this.enabled;
 }


 

    
}
