package com.workshop.mapper;

import com.workshop.dto.UserDTO;
import com.workshop.model.Role;
import com.workshop.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setEnabled(user.getEnabled());
        dto.setCustomerId(user.getCustomerId());

        dto.setRoles(
                user.getRoles()
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet())
        );

        return dto;
    }
}

