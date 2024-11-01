package com.zett.recipeapi.entities;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false, columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(name = "description", columnDefinition = "NVARCHAR(500)")
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Recipe> recipes;
}
