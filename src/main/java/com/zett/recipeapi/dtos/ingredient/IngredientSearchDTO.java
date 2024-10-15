package com.zett.recipeapi.dtos.ingredient;

import com.zett.recipeapi.dtos.core.SearchDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientSearchDTO extends SearchDTO {
    private String keyword;
}