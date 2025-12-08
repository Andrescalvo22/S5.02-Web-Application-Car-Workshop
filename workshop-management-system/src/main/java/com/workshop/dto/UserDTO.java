package com.workshop.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private Set<String> roles;
    private Boolean enabled;
    private Long customerId;
}

