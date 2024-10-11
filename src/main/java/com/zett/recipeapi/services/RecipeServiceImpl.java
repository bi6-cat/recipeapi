package com.zett.recipeapi.services;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zett.recipeapi.dtos.category.CategoryDTO;
import com.zett.recipeapi.dtos.ingredient.IngredientDTO;
import com.zett.recipeapi.dtos.recipe.RecipeAddIngredientDTO;
import com.zett.recipeapi.dtos.recipe.RecipeAddIngredientListDTO;
import com.zett.recipeapi.dtos.recipe.RecipeCreateDTO;
import com.zett.recipeapi.dtos.recipe.RecipeDTO;
import com.zett.recipeapi.dtos.recipe.RecipeEditDTO;
import com.zett.recipeapi.entities.Recipe;
import com.zett.recipeapi.entities.RecipeIngredient;
import com.zett.recipeapi.entities.RecipeIngredientId;
import com.zett.recipeapi.repositories.CategoryRepository;
import com.zett.recipeapi.repositories.IngredientRepository;
import com.zett.recipeapi.repositories.RecipeIngredientRepository;
import com.zett.recipeapi.repositories.RecipeRepository;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    // Inject RecipeRepository via constructor
    public RecipeServiceImpl(RecipeRepository recipeRepository, CategoryRepository categoryRepository,
            IngredientRepository ingredientRepository, RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    @Override
    public List<RecipeDTO> findAll() {
        var recipes = recipeRepository.findAll();

        var recipeDTOs = recipes.stream().map(recipe -> {
            var recipeDTO = new RecipeDTO();
            recipeDTO.setId(recipe.getId());
            recipeDTO.setTitle(recipe.getTitle());
            recipeDTO.setDescription(recipe.getDescription());
            recipeDTO.setImage(recipe.getImage());
            recipeDTO.setPrepTime(recipe.getPrepTime());
            recipeDTO.setCookTime(recipe.getCookTime());
            recipeDTO.setServings(recipe.getServings());

            // Check if entity recipe has category
            if (recipe.getCategory() != null) {
                // Convert Category to CategoryDTO
                var categoryDTO = new CategoryDTO();
                categoryDTO.setId(recipe.getCategory().getId());
                categoryDTO.setName(recipe.getCategory().getName());
                categoryDTO.setDescription(recipe.getCategory().getDescription());

                // Set categoryDTO to recipeDTO
                recipeDTO.setCategory(categoryDTO);

            }
            return recipeDTO;
        }).toList();

        return recipeDTOs;
    }

    @Override
    public List<RecipeDTO> findAll(String keyword) {
        // Find recipe by keyword
        Specification<Recipe> specification = (root, query, criteriaBuilder) -> {
            // Neu keyword null thi tra ve null
            if (keyword == null) {
                return null;
            }

            // Neu keyword khong null
            // WHERE LOWER(name) LIKE %keyword%
            Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
                    "%" + keyword.toLowerCase() + "%");

            // WHERE LOWER(description) LIKE %keyword%
            Predicate desPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                    "%" + keyword.toLowerCase() + "%");

            // WHERE LOWER(name) LIKE %keyword% OR LOWER(description) LIKE %keyword%
            return criteriaBuilder.or(titlePredicate, desPredicate);
        };

        var recipes = recipeRepository.findAll(specification);

        // Covert List<Recipe> to List<RecipeDTO>
        var recipeDTOs = recipes.stream().map(recipe -> {
            var recipeDTO = new RecipeDTO();
            recipeDTO.setId(recipe.getId());
            recipeDTO.setTitle(recipe.getTitle());
            recipeDTO.setDescription(recipe.getDescription());
            recipeDTO.setImage(recipe.getImage());
            recipeDTO.setPrepTime(recipe.getPrepTime());
            recipeDTO.setCookTime(recipe.getCookTime());
            recipeDTO.setServings(recipe.getServings());

            // Check if entity recipe has category
            if (recipe.getCategory() != null) {
                // Convert Category to CategoryDTO
                var categoryDTO = new CategoryDTO();
                categoryDTO.setId(recipe.getCategory().getId());
                categoryDTO.setName(recipe.getCategory().getName());
                categoryDTO.setDescription(recipe.getCategory().getDescription());

                // Set categoryDTO to recipeDTO
                recipeDTO.setCategory(categoryDTO);
            }

            if (recipe.getRecipeIngredients() != null) {
                var ingredientDTOs = recipe.getRecipeIngredients().stream().map(recipeIngredient -> {
                    var ingredientDTO = new IngredientDTO();
                    ingredientDTO.setId(recipeIngredient.getIngredient().getId());
                    ingredientDTO.setName(recipeIngredient.getIngredient().getName());
                    return ingredientDTO;
                }).toList();

                recipeDTO.setIngredients(ingredientDTOs);
            }

            return recipeDTO;
        }).toList();

        return recipeDTOs;
    }

    @Override
    public Page<RecipeDTO> findAll(String keyword, Pageable pageable) {
        // Find recipe by keyword
        Specification<Recipe> specification = (root, query, criteriaBuilder) -> {
            // Neu keyword null thi tra ve null
            if (keyword == null) {
                return null;
            }

            // Neu keyword khong null
            // WHERE LOWER(name) LIKE %keyword%
            Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
                    "%" + keyword.toLowerCase() + "%");

            // WHERE LOWER(description) LIKE %keyword%
            Predicate desPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                    "%" + keyword.toLowerCase() + "%");

            // WHERE LOWER(name) LIKE %keyword% OR LOWER(description) LIKE %keyword%
            return criteriaBuilder.or(titlePredicate, desPredicate);
        };

        var recipes = recipeRepository.findAll(specification, pageable);

        // Covert Page<Recipe> to Page<RecipeDTO>
        var recipeDTOs = recipes.map(recipe -> {
            var recipeDTO = new RecipeDTO();
            recipeDTO.setId(recipe.getId());
            recipeDTO.setTitle(recipe.getTitle());
            recipeDTO.setDescription(recipe.getDescription());
            recipeDTO.setImage(recipe.getImage());
            recipeDTO.setPrepTime(recipe.getPrepTime());
            recipeDTO.setCookTime(recipe.getCookTime());
            recipeDTO.setServings(recipe.getServings());

            // Check if entity recipe has category
            if (recipe.getCategory() != null) {
                // Convert Category to CategoryDTO
                var categoryDTO = new CategoryDTO();
                categoryDTO.setId(recipe.getCategory().getId());
                categoryDTO.setName(recipe.getCategory().getName());
                categoryDTO.setDescription(recipe.getCategory().getDescription());

                // Set categoryDTO to recipeDTO
                recipeDTO.setCategory(categoryDTO);
            }

            if (recipe.getRecipeIngredients() != null) {
                var ingredientDTOs = recipe.getRecipeIngredients().stream().map(recipeIngredient -> {
                    var ingredientDTO = new IngredientDTO();
                    ingredientDTO.setId(recipeIngredient.getIngredient().getId());
                    ingredientDTO.setName(recipeIngredient.getIngredient().getName());
                    return ingredientDTO;
                }).toList();

                recipeDTO.setIngredients(ingredientDTOs);
            }

            return recipeDTO;
        });

        return recipeDTOs;
    }

    @Override
    public RecipeDTO findById(UUID id) {
        var recipe = recipeRepository.findById(id).orElse(null);

        if (recipe == null) {
            return null;
        }

        var recipeDTO = new RecipeDTO();
        recipeDTO.setId(recipe.getId());
        recipeDTO.setTitle(recipe.getTitle());
        recipeDTO.setDescription(recipe.getDescription());
        recipeDTO.setImage(recipe.getImage());
        recipeDTO.setPrepTime(recipe.getPrepTime());
        recipeDTO.setCookTime(recipe.getCookTime());
        recipeDTO.setServings(recipe.getServings());

        // Check if entity recipe has category
        if (recipe.getCategory() != null) {
            // Convert Category to CategoryDTO
            var categoryDTO = new CategoryDTO();
            categoryDTO.setId(recipe.getCategory().getId());
            categoryDTO.setName(recipe.getCategory().getName());
            categoryDTO.setDescription(recipe.getCategory().getDescription());

            // Set categoryDTO to recipeDTO
            recipeDTO.setCategory(categoryDTO);
        }

        if (recipe.getRecipeIngredients() != null) {
            var ingredientDTOs = recipe.getRecipeIngredients().stream().map(recipeIngredient -> {
                var ingredientDTO = new IngredientDTO();
                ingredientDTO.setId(recipeIngredient.getIngredient().getId());
                ingredientDTO.setName(recipeIngredient.getIngredient().getName());
                return ingredientDTO;
            }).toList();

            recipeDTO.setIngredients(ingredientDTOs);
        }

        return recipeDTO;
    }

    @Override
    public RecipeDTO create(RecipeCreateDTO recipeCreateDTO) {
        // Kiem tra recipeDTO null
        if (recipeCreateDTO == null) {
            throw new IllegalArgumentException("Recipe is required");
        }

        // Checl if recipe name is existed
        var existedRecipe = recipeRepository.findByTitle(recipeCreateDTO.getTitle());
        if (existedRecipe != null) {
            throw new IllegalArgumentException("Recipe name is existed");
        }

        // Convert RecipeDTO to Recipe
        var recipe = new Recipe();
        recipe.setTitle(recipeCreateDTO.getTitle());
        recipe.setDescription(recipeCreateDTO.getDescription());
        recipe.setImage(recipeCreateDTO.getImage());
        recipe.setPrepTime(recipeCreateDTO.getPrepTime());
        recipe.setCookTime(recipeCreateDTO.getCookTime());
        recipe.setServings(recipeCreateDTO.getServings());

        // Check if recipeCreateDTO.getCategoryId() is not null
        if (recipeCreateDTO.getCategoryId() != null) {
            // Find category by id
            var category = categoryRepository.findById(recipeCreateDTO.getCategoryId()).orElse(null);

            // Check if category is not null
            if (category != null) {
                // Set category to recipe
                recipe.setCategory(category);
            }
        }

        // Save recipe
        recipe = recipeRepository.save(recipe);

        // Convert Recipe to RecipeDTO
        var newRecipeDTO = new RecipeDTO();
        newRecipeDTO.setId(recipe.getId());
        newRecipeDTO.setTitle(recipe.getTitle());
        newRecipeDTO.setDescription(recipe.getDescription());
        newRecipeDTO.setImage(recipe.getImage());
        newRecipeDTO.setPrepTime(recipe.getPrepTime());
        newRecipeDTO.setCookTime(recipe.getCookTime());
        newRecipeDTO.setServings(recipe.getServings());

        // Check if entity recipe has category
        if (recipe.getCategory() != null) {
            // Convert Category to CategoryDTO
            var categoryDTO = new CategoryDTO();
            categoryDTO.setId(recipe.getCategory().getId());
            categoryDTO.setName(recipe.getCategory().getName());
            categoryDTO.setDescription(recipe.getCategory().getDescription());

            // Set categoryDTO to recipeDTO
            newRecipeDTO.setCategory(categoryDTO);
        }

        return newRecipeDTO;
    }

    @Override
    public RecipeDTO update(UUID id, RecipeEditDTO recipeEditDTO) {
        if (recipeEditDTO == null) {
            throw new IllegalArgumentException("Recipe is required");
        }

        // Check if recipe name is existed
        var existedRecipe = recipeRepository.findByTitle(recipeEditDTO.getTitle());
        if (existedRecipe != null && !existedRecipe.getId().equals(id)) {
            throw new IllegalArgumentException("Recipe name is existed");
        }

        // Find recipe by id - Managed
        var recipe = recipeRepository.findById(id).orElse(null);

        if (recipe == null) {
            throw new IllegalArgumentException("Recipe not found");
        }

        // Update recipe
        recipe.setTitle(recipeEditDTO.getTitle());
        recipe.setDescription(recipeEditDTO.getDescription());
        recipe.setImage(recipeEditDTO.getImage());
        recipe.setPrepTime(recipeEditDTO.getPrepTime());
        recipe.setCookTime(recipeEditDTO.getCookTime());
        recipe.setServings(recipeEditDTO.getServings());

        // Check if recipeEditDTO.getCategoryId() is not null
        if (recipeEditDTO.getCategoryId() != null) {
            // Find category by id
            var category = categoryRepository.findById(recipeEditDTO.getCategoryId()).orElse(null);

            // Check if category is not null
            if (category != null) {
                // Set category to recipe
                recipe.setCategory(category);
            }
        }

        // Save recipe => update
        recipe = recipeRepository.save(recipe);

        // Convert Recipe to RecipeDTO
        var updatedRecipeDTO = new RecipeDTO();
        updatedRecipeDTO.setId(recipe.getId());
        updatedRecipeDTO.setTitle(recipe.getTitle());
        updatedRecipeDTO.setDescription(recipe.getDescription());
        updatedRecipeDTO.setImage(recipe.getImage());
        updatedRecipeDTO.setPrepTime(recipe.getPrepTime());
        updatedRecipeDTO.setCookTime(recipe.getCookTime());
        updatedRecipeDTO.setServings(recipe.getServings());

        // Check if entity recipe has category
        if (recipe.getCategory() != null) {
            // Convert Category to CategoryDTO
            var categoryDTO = new CategoryDTO();
            categoryDTO.setId(recipe.getCategory().getId());
            categoryDTO.setName(recipe.getCategory().getName());
            categoryDTO.setDescription(recipe.getCategory().getDescription());

            // Set categoryDTO to recipeDTO
            updatedRecipeDTO.setCategory(categoryDTO);
        }

        return updatedRecipeDTO;
    }

    @Override
    public boolean delete(UUID id) {
        // Find recipe by id - Managed
        var recipe = recipeRepository.findById(id).orElse(null);

        if (recipe == null) {
            throw new IllegalArgumentException("Recipe not found");
        }

        // Delete recipe
        recipeRepository.delete(recipe);

        // Check if recipe is deleted
        return !recipeRepository.existsById(id);
    }

    @Override
    public RecipeAddIngredientDTO addIngredient(UUID id,
            RecipeAddIngredientDTO recipeAddIngredientDTO) {
        if (recipeAddIngredientDTO == null) {
            throw new IllegalArgumentException("RecipeAddIngredientDTO is required");
        }

        // Find recipe by id
        var recipe = recipeRepository.findById(id).orElse(null);

        if (recipe == null) {
            throw new IllegalArgumentException("Recipe not found");
        }

        // Find ingredient by id
        var ingredient = ingredientRepository.findById(recipeAddIngredientDTO.getIngredientId()).orElse(null);

        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient not found");
        }

        // Add ingredient to recipe
        var recipeIngredientId = new RecipeIngredientId(recipe.getId(), ingredient.getId());

        var recipeIngredient = new RecipeIngredient();
        recipeIngredient.setId(recipeIngredientId);
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setAmount(recipeAddIngredientDTO.getAmount());

        // Save recipe
        recipeIngredient = recipeIngredientRepository.save(recipeIngredient);

        return recipeIngredient != null ? recipeAddIngredientDTO : null;
    }

    @Override
    public RecipeAddIngredientListDTO addIngredient(UUID id, RecipeAddIngredientListDTO recipeAddIngredientListDTO) {
        if (recipeAddIngredientListDTO == null) {
            throw new IllegalArgumentException("RecipeAddIngredientListDTO is required");
        }
        var recipe = recipeRepository.findById(id).orElse(null);

        if (recipe == null) {
            throw new IllegalArgumentException("Recipe not found");
        }

        var recipeIngredients = recipeAddIngredientListDTO.getIngredients().stream().map(recipeAddIngredientDTO -> {
            var ingredient = ingredientRepository.findById(recipeAddIngredientDTO.getIngredientId()).orElse(null);

            if (ingredient == null) {
                throw new IllegalArgumentException("Ingredient not found");
            }

            var recipeIngredientId = new RecipeIngredientId(recipe.getId(), ingredient.getId());

            var recipeIngredient = new RecipeIngredient();
            recipeIngredient.setId(recipeIngredientId);
            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setAmount(recipeAddIngredientDTO.getAmount());
            
            recipeIngredient = recipeIngredientRepository.save(recipeIngredient);

            return recipeIngredient;
        }).toList();

        return recipeIngredients != null ? recipeAddIngredientListDTO : null;
        
    }
}
