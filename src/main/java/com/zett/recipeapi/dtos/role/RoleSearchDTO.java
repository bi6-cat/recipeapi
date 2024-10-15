package com.zett.recipeapi.dtos.role;

import com.zett.recipeapi.dtos.core.SearchDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleSearchDTO extends SearchDTO {
    private String keyword;
}
