package com.example.DemoSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public WebSecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrd -> csrd.disable()) // Disables CSRF protection
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("register","login").permitAll()
                                .anyRequest().authenticated()
                )
                //.formLogin(Customizer.withDefaults()) // Enables form-based login
                .httpBasic(Customizer.withDefaults()) // Enables HTTP Basic authentication
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Adds a custom JWT authentication filter before the UsernamePasswordAuthenticationFilter

        return httpSecurity.build();
    }

    //@Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1
                = User.withUsername("durga")
                .password("{noop}durga")
                .roles("USER")
                .build();

        UserDetails user2
                = User.withUsername("bhanu")
                .password("{noop}bhanu")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(14); // BCryptPasswordEncoder is used to encode passwords securely
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Returns the AuthenticationManager from the provided AuthenticationConfiguration
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // DaoAuthenticationProvider is used to authenticate users against a database or other persistent storage
        provider.setUserDetailsService(userDetailsService); // This sets the UserDetailsService that will be used to load user-specific data
        //provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); // NoOpPasswordEncoder is used for simplicity, not recommended for production
        provider.setPasswordEncoder(bCryptPasswordEncoder()); // Sets the password encoder to BCryptPasswordEncoder for secure password handling

        return provider; // Returns the configured AuthenticationProvider
    }
}