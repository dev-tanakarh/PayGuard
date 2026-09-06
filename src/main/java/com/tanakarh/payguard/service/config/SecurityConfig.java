package com.tanakarh.payguard.service.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Only disable if using stateless JWT auth
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(POST, "/api/customers/**").permitAll()  // Allow registration
                .requestMatchers(POST, "/api/merchant/**").permitAll()
                .requestMatchers(GET, "/api/auth/**").permitAll()
                .requestMatchers("/app/v1/admin/**").hasRole("ADMIN")  // ✅ Admin only
                .requestMatchers("/api/customers/**").hasAnyRole("CUSTOMER")
                .requestMatchers("/api/merchant/**").hasRole("MERCHANT")
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());
        return http.build();
    }
}
