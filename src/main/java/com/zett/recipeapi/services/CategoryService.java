package com.zett.recipeapi.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zett.recipeapi.dtos.category.CategoryCreateDTO;
import com.zett.recipeapi.dtos.category.CategoryDTO;

public interface CategoryService {
    List<CategoryDTO> findAll();

    List<CategoryDTO> findAll(String keyword);

    Page<CategoryDTO> findAll(String keyword, Pageable pageable);

    CategoryDTO findById(UUID id);

    CategoryDTO create(CategoryCreateDTO categoryCreateDTO);

    CategoryDTO update(UUID id, CategoryDTO categoryDTO);

    boolean delete(UUID id);
}