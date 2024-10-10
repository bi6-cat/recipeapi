package com.zett.recipeapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zett.recipeapi.entities.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe,UUID>, JpaSpecificationExecutor<Recipe> {
    Recipe findByTitle(String title);
}
