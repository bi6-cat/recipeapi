package com.zett.recipeapi.dtos.recipe;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeAddIngredientDTO {
    @NotNull(message = "Recipe ID is required")
    private UUID ingredientId;
    
    @NotNull(message = "Amount is required")
    private String amount;
}
