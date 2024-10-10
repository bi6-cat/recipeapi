package com.zett.recipeapi.dtos.ingredient;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientCreateBatchDTO {
    
    @NotNull(message = "Name is required")
    private List<IngredientCreateDTO> ingredients;
}
