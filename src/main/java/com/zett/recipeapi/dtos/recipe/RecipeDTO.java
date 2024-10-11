package com.zett.recipeapi.dtos.recipe;

import java.util.List;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import com.zett.recipeapi.dtos.category.CategoryDTO;
import com.zett.recipeapi.dtos.ingredient.IngredientDTO;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipes")
public class RecipeDTO {
    private UUID id;

    @NotNull(message = "Title is required")
    @NotBlank(message = "Title is required")
    @NotEmpty(message = "Title is required")
    @Length(min = 3, max = 255, message = "Tile must be between 3 and 255 characters")
    private String title;

    private String description;

    private String image; // URL to image

    @PositiveOrZero
    private Integer prepTime; // in minutes
    
    @PositiveOrZero
    private Integer cookTime; // in minutes
   
    @PositiveOrZero
    private Integer servings; // number of servings

    private CategoryDTO category;

    private List<IngredientDTO> ingredients;

}
