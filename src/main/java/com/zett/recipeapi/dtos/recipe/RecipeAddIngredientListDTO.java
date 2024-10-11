package com.zett.recipeapi.dtos.recipe;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeAddIngredientListDTO {
    @NotNull(message = "Ingredients are required")
    private List<RecipeAddIngredientDTO> ingredients;
}
