package com.zett.recipeapi.dtos.user;

import com.zett.recipeapi.dtos.core.SearchDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDTO extends SearchDTO {
    private String keyword;
}