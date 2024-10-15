package com.zett.recipeapi.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zett.recipeapi.dtos.user.UserCreateDTO;
import com.zett.recipeapi.dtos.user.UserDTO;
import com.zett.recipeapi.dtos.user.UserEditDTO;

public interface UserService {
    List<UserDTO> findAll();

    List<UserDTO> findAll(String keyword);

    Page<UserDTO> findAll(String keyword, Pageable pageable);

    UserDTO findById(UUID id);

    UserDTO create(UserCreateDTO userCreateDTO);

    UserDTO update(UUID id, UserEditDTO userEditDTO);

    boolean delete(UUID id);
}

