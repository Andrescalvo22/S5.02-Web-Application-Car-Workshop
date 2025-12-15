package com.workshop.service.impl;

import com.workshop.dto.authentication.AuthRequest;
import com.workshop.dto.authentication.AuthResponse;
import com.workshop.dto.authentication.RegisterRequest;
import com.workshop.model.Customer;
import com.workshop.model.Role;
import com.workshop.model.User;
import com.workshop.repository.CustomerRepository;
import com.workshop.repository.UserRepository;
import com.workshop.security.JwtProvider;
import com.workshop.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomerRepository customerRepository;

    @Override
    public AuthResponse login(AuthRequest request) {

        System.out.println("====== LOGIN DEBUG ======");
        System.out.println("EMAIL RECIBIDO: " + request.getEmail());
        System.out.println("PASSWORD RECIBIDO: " + request.getPassword());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Incorrect email or password"));

        System.out.println("HASH EN BD: " + user.getPassword());
        System.out.println("PASSWORD MATCH?: " + passwordEncoder.matches(request.getPassword(), user.getPassword()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect email or password");
        }

        Set<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        String token = jwtProvider.generateToken(user.getEmail(), roles);

        return new AuthResponse(token);
    }

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        Customer customer = Customer.builder()
                .name(request.getName() + " " + request.getLastName())
                .email(request.getEmail())
                .telephone("N/A")
                .build();

        customerRepository.save(customer);

        User newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(Set.of(Role.ROLE_USER))
                .customerId(customer.getId())
                .build();

        userRepository.save(newUser);

        Set<String> roles = newUser.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        String token = jwtProvider.generateToken(newUser.getEmail(), roles);

        return new AuthResponse(token);
    }
}

