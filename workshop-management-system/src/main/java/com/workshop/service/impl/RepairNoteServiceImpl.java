package com.workshop.service.impl;

import com.workshop.dto.RepairNoteRequest;
import com.workshop.dto.RepairNoteResponse;
import com.workshop.model.RepairNote;
import com.workshop.model.RepairOrder;
import com.workshop.model.User;
import com.workshop.repository.RepairNoteRepository;
import com.workshop.repository.RepairOrderRepository;
import com.workshop.repository.UserRepository;
import com.workshop.service.RepairNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RepairNoteServiceImpl implements RepairNoteService {

    private final RepairOrderRepository repairOrderRepository;
    private final RepairNoteRepository repairNoteRepository;
    private final UserRepository userRepository;

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch(r -> r.name().equals("ROLE_ADMIN"));
    }

    private void checkAccessToOrder(RepairOrder order, User user) {
        Long customerId = order.getCar().getCustomer().getId();

        if (!isAdmin(user) && (user.getCustomerId() == null || !customerId.equals(user.getCustomerId()))) {
            throw new AccessDeniedException("You cannot access notes of this repair order");
        }
    }

    @Override
    public List<RepairNoteResponse> getNotes(Long repairOrderId) {

        User user = getAuthenticatedUser();

        RepairOrder order = repairOrderRepository.findById(repairOrderId)
                .orElseThrow(() -> new RuntimeException("Repair order not found"));

        checkAccessToOrder(order, user);

        return repairNoteRepository.findByRepairOrderIdOrderByCreatedAtDesc(repairOrderId)
                .stream()
                .map(n -> RepairNoteResponse.builder()
                        .id(n.getId())
                        .text(n.getText())
                        .author(n.getAuthor())
                        .createdAt(n.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    public RepairNoteResponse addNote(Long repairOrderId, RepairNoteRequest request) {

        User user = getAuthenticatedUser();

        RepairOrder order = repairOrderRepository.findById(repairOrderId)
                .orElseThrow(() -> new RuntimeException("Repair order not found"));

        checkAccessToOrder(order, user);

        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new RuntimeException("Note text cannot be empty");
        }

        RepairNote note = RepairNote.builder()
                .text(request.getText().trim())
                .author(user.getEmail())
                .createdAt(LocalDateTime.now())
                .repairOrder(order)
                .build();

        RepairNote saved = repairNoteRepository.save(note);

        return RepairNoteResponse.builder()
                .id(saved.getId())
                .text(saved.getText())
                .author(saved.getAuthor())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}

