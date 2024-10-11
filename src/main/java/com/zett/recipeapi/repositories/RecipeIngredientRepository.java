package com.zett.recipeapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zett.recipeapi.entities.RecipeIngredient;
import com.zett.recipeapi.entities.RecipeIngredientId;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, RecipeIngredientId> {
}
