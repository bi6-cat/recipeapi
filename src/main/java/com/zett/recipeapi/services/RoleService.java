package com.zett.recipeapi.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zett.recipeapi.dtos.role.RoleCreateDTO;
import com.zett.recipeapi.dtos.role.RoleDTO;

public interface RoleService {
    List<RoleDTO> findAll();

    List<RoleDTO> findAll(String keyword);

    Page<RoleDTO> findAll(String keyword, Pageable pageable);

    RoleDTO findById(UUID id);

    RoleDTO create(RoleCreateDTO roleCreateDTO);

    RoleDTO update(UUID id, RoleDTO roleDTO);

    boolean delete(UUID id);
}