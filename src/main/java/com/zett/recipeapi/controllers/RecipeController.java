package com.zett.recipeapi.controllers;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.zett.recipeapi.dtos.core.SortDirection;
import com.zett.recipeapi.dtos.ingredient.IngredientSearchDTO;
import com.zett.recipeapi.dtos.recipe.RecipeAddIngredientDTO;
import com.zett.recipeapi.dtos.recipe.RecipeAddIngredientListDTO;
import com.zett.recipeapi.dtos.recipe.RecipeCreateDTO;
import com.zett.recipeapi.dtos.recipe.RecipeDTO;
import com.zett.recipeapi.dtos.recipe.RecipeEditDTO;
import com.zett.recipeapi.services.RecipeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Recipe", description = "Recipe API")
@RequestMapping("/api/v1/recipes")
public class RecipeController {
    private final RecipeService recipeService;
    private final PagedResourcesAssembler<RecipeDTO> pagedResourcesAssembler;

    public RecipeController(RecipeService recipeService, PagedResourcesAssembler<RecipeDTO> pagedResourcesAssembler) {
        this.recipeService = recipeService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all recipes or search recipes by keyword")
    @ApiResponse(responseCode = "200", description = "Return all recipes or search recipes by keyword")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "title") String sortBy, // Xac dinh truong sap xep
            @RequestParam(required = false, defaultValue = "asc") String order, // Xac dinh chieu sap xep
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size) {
        // Check sort order
        Pageable pageable = null;

        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        // Search recipe by keyword and paging
        var recipes = recipeService.findAll(keyword, pageable);

        // Convert to PagedModel - Enhance data with HATEOAS - Easy to navigate with
        // links
        var pagedModel = pagedResourcesAssembler.toModel(recipes);

        return ResponseEntity.ok(pagedModel);
    }

    // Get by id - GetMapping - /api/v1/recipes/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Get recipe by id")
    @ApiResponse(responseCode = "200", description = "Return recipe by id")
    @ApiResponse(responseCode = "404", description = "Recipe not found")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        var recipe = recipeService.findById(id);
        // Check if recipe is null => return 404 Not Found
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if recipe is not null => return 200 OK with recipe
        return ResponseEntity.ok(recipe);
    }

    @PostMapping("/search")
    @Operation(summary = "Get all recipes or search recipes by keyword")
    @ApiResponse(responseCode = "200", description = "Return all recipes or search recipes by keyword")
    public ResponseEntity<?> search(@RequestBody IngredientSearchDTO recipeSearchDTO) {
        // Check sort order
        Pageable pageable = null;

        if (recipeSearchDTO.getOrder().equals(SortDirection.ASC)) {
            pageable = PageRequest.of(recipeSearchDTO.getPage(), recipeSearchDTO.getSize(),
                    Sort.by(recipeSearchDTO.getSortBy()).ascending());
        } else {
            pageable = PageRequest.of(recipeSearchDTO.getPage(), recipeSearchDTO.getSize(),
                    Sort.by(recipeSearchDTO.getSortBy()).descending());
        }

        // Search recipe by keyword and paging
        var recipes = recipeService.findAll(recipeSearchDTO.getKeyword(), pageable);

        // Convert to PagedModel - Enhance data with HATEOAS - Easy to navigate with
        // links
        var pagedModel = pagedResourcesAssembler.toModel(recipes);

        return ResponseEntity.ok(pagedModel);
    }


    // Create - PostMapping - /api/v1/recipes
    @PostMapping
    @Operation(summary = "Create new recipe")
    @ApiResponse(responseCode = "201", description = "Create new recipe")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    public ResponseEntity<?> create(@Valid @RequestBody RecipeCreateDTO recipeCreateDTO,
            BindingResult bindingResult) {
        // Validate recipeCreateDTO
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var newRecipe = recipeService.create(recipeCreateDTO);

        // Check if newRecipe is null => return 400 Bad Request
        if (newRecipe == null) {
            return ResponseEntity.badRequest().build();
        }

        // Check if newRecipe is not null => return 201 Created with newRecipe
        return ResponseEntity.status(201).body(newRecipe);
    }


    // Update - PutMapping - /api/v1/recipes/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Update recipe by id")
    @ApiResponse(responseCode = "200", description = "Update recipe by id")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    public ResponseEntity<?> edit(
            @PathVariable UUID id,
            @Valid @RequestBody RecipeEditDTO recipeEditDTO,
            BindingResult bindingResult) {
        // Validate recipeDTO
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var updatedRecipeDTO = recipeService.update(id, recipeEditDTO);

        // Check if updatedRecipe is null => return 400 Bad Request
        if (updatedRecipeDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        // Check if updatedRecipe is not null => return 201 Created with
        // updatedRecipe
        return ResponseEntity.ok(updatedRecipeDTO);
    }

    // Delete - DeleteMapping - /api/v1/recipes/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete recipe by id")
    @ApiResponse(responseCode = "200", description = "Delete recipe by id")
    @ApiResponse(responseCode = "404", description = "Recipe not found")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        var existedRecipe = recipeService.findById(id);
        // Check if recipe is null => return 404 Not Found
        if (existedRecipe == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if recipe is not null => delete recipe
        var isDeleted = recipeService.delete(id);

        return ResponseEntity.ok(isDeleted);
    }

    @PostMapping("/{id}/add-ingredient/")
    @Operation(summary = "Add ingredient to recipe")
    @ApiResponse(responseCode = "200", description = "Add ingredient to recipe")
    @ApiResponse(responseCode = "404", description = "Recipe or Ingredient not found")
    public ResponseEntity<?> addIngredient(@PathVariable UUID id,
            @Valid @RequestBody RecipeAddIngredientDTO recipeAddIngredientDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var result = recipeService.addIngredient(id, recipeAddIngredientDTO);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/add-list-ingredient/")
    @Operation(summary = "Add ingredient to recipe")
    @ApiResponse(responseCode = "200", description = "Add ingredient to recipe")
    @ApiResponse(responseCode = "404", description = "Recipe or Ingredient not found")
    public ResponseEntity<?> addListIngredient(@PathVariable UUID id,
            @Valid @RequestBody RecipeAddIngredientListDTO recipeAddIngredientListDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var result = recipeService.addIngredient(id, recipeAddIngredientListDTO);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

}
