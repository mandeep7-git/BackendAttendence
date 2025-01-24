package com.attendance.BackendAttendanceRT.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.attendance.BackendAttendanceRT.Model.User;

@Service
public class JwtService {
  // @Value("${security.jwt.secret-key}")
   private String secretKey = "thisIsA32CharacterLongSecureKey";
   @Value("${security.jwt.expiration-time}")
   private long jwtExpiration;

   public JwtService() {
}

public String extractUsername(String token) {
   return (String)this.extractClaim(token, Claims::getSubject);
}

public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
   Claims claims = this.extractAllClaims(token);
   return claimsResolver.apply(claims);
}

public String generateToken(UserDetails userDetails) {
   return this.generateToken(new HashMap(), userDetails);
}

public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
   return this.buildToken(extraClaims, userDetails, this.jwtExpiration);
}

public long getExpirationTime() {
   return this.jwtExpiration;
}

private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
   return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + expiration)).signWith(this.getSignInKey(), SignatureAlgorithm.HS256).compact();
}

public boolean isTokenValid(String token, UserDetails userDetails) {
   String username = this.extractUsername(token);
   return username.equals(userDetails.getUsername()) && !this.isTokenExpired(token);
}

private boolean isTokenExpired(String token) {
   return this.extractExpiration(token).before(new Date());
}

private Date extractExpiration(String token) {
   return (Date)this.extractClaim(token, Claims::getExpiration);
}

private Claims extractAllClaims(String token) {
   return (Claims)Jwts.parserBuilder().setSigningKey(this.getSignInKey()).build().parseClaimsJws(token).getBody();
}

// private Key getSignInKey() {
//    byte[] keyBytes = (byte[])Decoders.BASE64.decode(this.secretKey);
//    return Keys.hmacShaKeyFor(keyBytes);
// }


private Key getSignInKey() {
        // Hardcoded key with a minimum of 32 characters
        String secret = "thisIsA32CharacterLongSecureKey";
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
}

