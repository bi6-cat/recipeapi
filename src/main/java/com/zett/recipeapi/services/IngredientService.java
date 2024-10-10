package com.zett.recipeapi.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zett.recipeapi.dtos.ingredient.IngredientCreateDTO;
import com.zett.recipeapi.dtos.ingredient.IngredientDTO;

public interface IngredientService {
    List<IngredientDTO> findAll();

    List<IngredientDTO> findAll(String keyword);

    Page<IngredientDTO> findAll(String keyword, Pageable pageable);

    IngredientDTO findById(UUID id);

    IngredientDTO create(IngredientCreateDTO ingredientCreateDTO);

    IngredientDTO update(UUID id, IngredientDTO ingredientDTO);

    boolean delete(UUID id);
}