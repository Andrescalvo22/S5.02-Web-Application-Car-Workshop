package com.workshop.service.impl;

import com.workshop.dto.UserDTO;
import com.workshop.exception.UserNotFoundException;
import com.workshop.mapper.UserMapper;
import com.workshop.model.User;
import com.workshop.repository.UserRepository;
import com.workshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else {
            email = principal.toString();
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    private boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch(r -> r.name().equals("ROLE_ADMIN"));
    }

    @Override
    public UserDTO getMe() {
        return mapper.toDTO(getAuthenticatedUser());
    }

    @Override
    public UserDTO updateMe(UserDTO dto) {
        User user = getAuthenticatedUser();

        user.setEmail(dto.getEmail());
        user.setEnabled(dto.getEnabled());
        user.setCustomerId(dto.getCustomerId());

        return mapper.toDTO(userRepository.save(user));
    }

    @Override
    public List<UserDTO> getAll() {
        User user = getAuthenticatedUser();

        if (!isAdmin(user)) {
            throw new RuntimeException("Only admin can see all users");
        }

        return userRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public UserDTO getById(Long id) {
        User user = getAuthenticatedUser();

        if (!isAdmin(user)) {
            throw new RuntimeException("Only admin can see other users");
        }

        User found = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return mapper.toDTO(found);
    }

    @Override
    public UserDTO update(Long id, UserDTO dto) {
        User user = getAuthenticatedUser();

        if (!isAdmin(user)) {
            throw new RuntimeException("Only admin can update users");
        }

        User existing = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        existing.setEmail(dto.getEmail());
        existing.setEnabled(dto.getEnabled());
        existing.setCustomerId(dto.getCustomerId());

        return mapper.toDTO(userRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        User user = getAuthenticatedUser();

        if (!isAdmin(user)) {
            throw new RuntimeException("Only admin can delete users");
        }

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
    }
}

