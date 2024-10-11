package com.zett.recipeapi.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zett.recipeapi.dtos.recipe.RecipeAddIngredientDTO;
import com.zett.recipeapi.dtos.recipe.RecipeCreateDTO;
import com.zett.recipeapi.dtos.recipe.RecipeDTO;
import com.zett.recipeapi.dtos.recipe.RecipeEditDTO;

public interface RecipeService {
    List<RecipeDTO> findAll();

    List<RecipeDTO> findAll(String keyword);

    Page<RecipeDTO> findAll(String keyword, Pageable pageable);

    RecipeDTO findById(UUID id);

    RecipeDTO create(RecipeCreateDTO recipeCreateDTO);

    RecipeDTO update(UUID id, RecipeEditDTO recipeEditDTO);

    RecipeAddIngredientDTO addIngredient(UUID id, UUID ingredientId, RecipeAddIngredientDTO recipeAddIngredientDTO);

    boolean delete(UUID id);
}
