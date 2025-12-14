package com.workshop.controller;

import com.workshop.dto.CarDTO;
import com.workshop.dto.RepairOrderDTO;
import com.workshop.model.User;
import com.workshop.repository.UserRepository;
import com.workshop.service.CarService;
import com.workshop.service.RepairOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class ClientDashboardController {

    private final UserRepository userRepository;
    private final CarService carService;
    private final RepairOrderService repairOrderService;

    @GetMapping
    public ResponseEntity<User> getMyProfile(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/cars")
    public ResponseEntity<List<CarDTO>> getMyCars(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        Long customerId = user.getCustomerId();

        return ResponseEntity.ok(carService.getByCustomer(customerId));
    }

    @GetMapping("/repairs")
    public ResponseEntity<List<RepairOrderDTO>> getMyRepairs(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        Long customerId = user.getCustomerId();

        return ResponseEntity.ok(repairOrderService.getByCustomerId(customerId));
    }
}
