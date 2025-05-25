package com.example.DemoSecurity.config;

import com.example.DemoSecurity.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException { // This method is called for every request to check if the JWT is valid and set the authentication in the security context
        final String authHeader = request.getHeader("Authorization"); // Extract the Authorization header from the request
        if(authHeader == null || !authHeader.startsWith("Bearer ")) { // Check if the header is present and starts with "Bearer "
            filterChain.doFilter(request, response); // If not, continue with the filter chain without setting any authentication
            return;
        }

        final String jwt = authHeader.substring(7); // Extract the JWT token from the header by removing the "Bearer " prefix
        final String userName = jwtService.extractUsername(jwt); // Extract the username from the JWT token

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Get the current authentication from the security context

        if(userName != null && authentication == null) { // If the username is not null and there is no current authentication
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName); // Load the user details from the user details service using the username extracted from the JWT
            if(jwtService.isTokenValid(jwt, userDetails)) { // Check if the JWT is valid for the user details
                UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                ); // Create a new UsernamePasswordAuthenticationToken with the user details and set it as the authentication in the security context
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                ); // Set the details of the authentication token using the request
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Set the authentication token in the security context
            }
        }
        filterChain.doFilter(request, response); // Continue with the filter chain after setting the authentication
    }
}
