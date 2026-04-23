package com.nagarseva.backend.filter;

import com.nagarseva.backend.service.CustomUserDetailsService;
import com.nagarseva.backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token != null) {
            username = jwtService.getUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userdetails = customUserDetailsService.loadUserByUsername(username);

            if (jwtService.validateToken(token, userdetails.getUsername())) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userdetails,
                        null,
                        userdetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
         }

        filterChain.doFilter(request, response);
    }
}
