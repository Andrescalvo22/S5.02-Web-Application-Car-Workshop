package com.workshop.service;

import com.workshop.dto.UserDTO;
import java.util.List;

public interface UserService {

    UserDTO getMe();

    UserDTO updateMe(UserDTO dto);

    List<UserDTO> getAll();

    UserDTO getById(Long id);

    UserDTO update(Long id, UserDTO dto);

    void delete(Long id);
}

