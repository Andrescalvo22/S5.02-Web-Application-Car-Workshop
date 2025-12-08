package com.workshop.config;

import com.workshop.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers("/api/users/me").authenticated()

                        .requestMatchers(HttpMethod.DELETE, "/api/customers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/customers").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/cars/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/cars").hasRole("ADMIN")

                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/tasks").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                // JWT FILTER
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

