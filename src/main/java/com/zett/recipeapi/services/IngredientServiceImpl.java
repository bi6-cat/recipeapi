package com.zett.recipeapi.services;

import java.util.List;
import java.util.UUID;

import com.zett.recipeapi.dtos.ingredient.IngredientCreateBatchDTO;
import com.zett.recipeapi.dtos.ingredient.IngredientCreateDTO;
import com.zett.recipeapi.dtos.ingredient.IngredientDTO;
import com.zett.recipeapi.entities.Ingredient;
import com.zett.recipeapi.repositories.IngredientRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;

    // Inject IngredientRepository via constructor
    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<IngredientDTO> findAll() {
        var ingredients = ingredientRepository.findAll();

        var ingredientDTOs = ingredients.stream().map(ingredient -> {
            var ingredientDTO = new IngredientDTO();
            ingredientDTO.setId(ingredient.getId());
            ingredientDTO.setName(ingredient.getName());
            return ingredientDTO;
        }).toList();

        return ingredientDTOs;
    }

    @Override
    public List<IngredientDTO> findAll(String keyword) {
        // Find ingredient by keyword
        Specification<Ingredient> specification = (root, query, criteriaBuilder) -> {
            // Neu keyword null thi tra ve null
            if (keyword == null) {
                return null;
            }

            // WHERE LOWER(name) LIKE %keyword%
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    "%" + keyword.toLowerCase() + "%");
        };

        var ingredients = ingredientRepository.findAll(specification);

        // Covert List<Ingredient> to List<IngredientDTO>
        var ingredientDTOs = ingredients.stream().map(ingredient -> {
            var ingredientDTO = new IngredientDTO();
            ingredientDTO.setId(ingredient.getId());
            ingredientDTO.setName(ingredient.getName());
            return ingredientDTO;
        }).toList();

        return ingredientDTOs;
    }

    @Override
    public Page<IngredientDTO> findAll(String keyword, Pageable pageable) {
        // Find ingredient by keyword
        Specification<Ingredient> specification = (root, query, criteriaBuilder) -> {
            // Neu keyword null thi tra ve null
            if (keyword == null) {
                return null;
            }

            // WHERE LOWER(name) LIKE %keyword%
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    "%" + keyword.toLowerCase() + "%");
        };

        var ingredients = ingredientRepository.findAll(specification, pageable);

        // Covert Page<Ingredient> to Page<IngredientDTO>
        var ingredientDTOs = ingredients.map(ingredient -> {
            var ingredientDTO = new IngredientDTO();
            ingredientDTO.setId(ingredient.getId());
            ingredientDTO.setName(ingredient.getName());
            return ingredientDTO;
        });

        return ingredientDTOs;
    }

    @Override
    public IngredientDTO findById(UUID id) {
        var ingredient = ingredientRepository.findById(id).orElse(null);

        if (ingredient == null) {
            return null;
        }

        var ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(ingredient.getId());
        ingredientDTO.setName(ingredient.getName());

        return ingredientDTO;
    }

    @Override
    public IngredientDTO create(IngredientCreateDTO ingredientCreateDTO) {
        // Kiem tra ingredientDTO null
        if (ingredientCreateDTO == null) {
            throw new IllegalArgumentException("Ingredient is required");
        }

        // Checl if ingredient name is existed
        var existedIngredient = ingredientRepository.findByName(ingredientCreateDTO.getName());
        if (existedIngredient != null) {
            throw new IllegalArgumentException("Ingredient name is existed");
        }

        // Convert IngredientDTO to Ingredient
        var ingredient = new Ingredient();
        ingredient.setName(ingredientCreateDTO.getName());

        // Save ingredient
        ingredient = ingredientRepository.save(ingredient);

        // Convert Ingredient to IngredientDTO
        var newIngredientDTO = new IngredientDTO();
        newIngredientDTO.setId(ingredient.getId());
        newIngredientDTO.setName(ingredient.getName());
        
        return newIngredientDTO;
    }

    @Override
    public List<IngredientDTO> create(IngredientCreateBatchDTO ingredientCreateBatchDTO) {
        // Kiem tra ingredientDTO null
        if (ingredientCreateBatchDTO == null) {
            throw new IllegalArgumentException("Ingredient is required");
        }

        var ingredients = ingredientCreateBatchDTO.getIngredients().stream().map(ingredientCreateDTO -> {
            var ingredient = new Ingredient();
            ingredient.setName(ingredientCreateDTO.getName());
            return ingredient;
        }).toList();

        var result = ingredientRepository.saveAll(ingredients);

        var ingredientDTOs = result.stream().map(ingredient -> {
            var ingredientDTO = new IngredientDTO();
            ingredientDTO.setId(ingredient.getId());
            ingredientDTO.setName(ingredient.getName());
            return ingredientDTO;
        }).toList();

        return ingredientDTOs;
    }
    
    @Override
    public IngredientDTO update(UUID id, IngredientDTO ingredientDTO) {
        if (ingredientDTO == null) {
            throw new IllegalArgumentException("Ingredient is required");
        }

        // Checl if ingredient name is existed
        var existedIngredient = ingredientRepository.findByName(ingredientDTO.getName());
        if (existedIngredient != null) {
            throw new IllegalArgumentException("Ingredient name is existed");
        }
        
        // Find ingredient by id - Managed
        var ingredient = ingredientRepository.findById(id).orElse(null);

        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient not found");
        }

        // Update ingredient
        ingredient.setName(ingredientDTO.getName());

        // Save ingredient => update
        ingredient = ingredientRepository.save(ingredient);

        // Convert Ingredient to IngredientDTO
        var updatedIngredientDTO = new IngredientDTO();
        updatedIngredientDTO.setId(ingredient.getId());
        updatedIngredientDTO.setName(ingredient.getName());

        return updatedIngredientDTO;
    }

    @Override
    public boolean delete(UUID id) {
        // Find ingredient by id - Managed
        var ingredient = ingredientRepository.findById(id).orElse(null);

        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient not found");
        }

        // Delete ingredient
        ingredientRepository.delete(ingredient);

        // Check if ingredient is deleted
        return !ingredientRepository.existsById(id);
    }

}
