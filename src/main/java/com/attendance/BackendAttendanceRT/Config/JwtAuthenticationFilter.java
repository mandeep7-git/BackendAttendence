package com.attendance.BackendAttendanceRT.Config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.attendance.BackendAttendanceRT.Service.JwtService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
   private final HandlerExceptionResolver handlerExceptionResolver;
   private final JwtService jwtService;
   private final UserDetailsService userDetailsService;

   public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, HandlerExceptionResolver handlerExceptionResolver) {
      this.jwtService = jwtService;
      this.userDetailsService = userDetailsService;
      this.handlerExceptionResolver = handlerExceptionResolver;
   }

   protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
      String authHeader = request.getHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
         try {
            String jwt = authHeader.substring(7);
            String userEmail = this.jwtService.extractUsername(jwt);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (userEmail != null && authentication == null) {
               UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
               if (this.jwtService.isTokenValid(jwt, userDetails)) {
                  UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, (Object)null, userDetails.getAuthorities());
                  authToken.setDetails((new WebAuthenticationDetailsSource()).buildDetails(request));
                  SecurityContextHolder.getContext().setAuthentication(authToken);
               }
            }

            filterChain.doFilter(request, response);
         } catch (Exception var10) {
            this.handlerExceptionResolver.resolveException(request, response, (Object)null, var10);
         }

      } else {
         filterChain.doFilter(request, response);
      }
   }
}
