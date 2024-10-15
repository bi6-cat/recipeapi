package com.zett.recipeapi.dtos.recipe;

import com.zett.recipeapi.dtos.core.SearchDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSearchDTO extends SearchDTO {
    private String keyword;
    
    private String categoryName;
}
