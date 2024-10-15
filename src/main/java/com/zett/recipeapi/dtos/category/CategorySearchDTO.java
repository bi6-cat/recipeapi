package com.zett.recipeapi.dtos.category;

import com.zett.recipeapi.dtos.core.SearchDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategorySearchDTO extends SearchDTO {
    private String keyword;
}
