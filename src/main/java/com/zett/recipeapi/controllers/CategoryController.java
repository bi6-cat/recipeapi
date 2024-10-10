package com.zett.recipeapi.controllers;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.zett.recipeapi.dtos.category.CategoryCreateDTO;
import com.zett.recipeapi.dtos.category.CategoryDTO;
import com.zett.recipeapi.services.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "categories", description = "The Category API")
public class CategoryController {
    private final CategoryService categoryService;
    private final PagedResourcesAssembler<CategoryDTO> PagedResourcesAssembler;

    public CategoryController(CategoryService categoryService,
            PagedResourcesAssembler<CategoryDTO> pagedResourcesAssembler) {
        this.categoryService = categoryService;
        this.PagedResourcesAssembler = pagedResourcesAssembler;
    }

    // get all categories
    @GetMapping
    @Operation(summary = "Get all categories or search categories by keyword")
    @ApiResponse(responseCode = "200", description = "Return all categories or search categories by keyword")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "2") Integer size) {
        PageRequest pageable = null;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        var categories = categoryService.findAll(keyword, pageable);

        var pageModel = PagedResourcesAssembler.toModel(categories);

        return ResponseEntity.ok(pageModel);
    }

    // get category by id
    @GetMapping("/{id}")
    @Operation(summary = "Get category by id")
    @ApiResponse(responseCode = "200", description = "Return category by id")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        var category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    @Operation(summary = "Create new category")
    @ApiResponse(responseCode = "201", description = "Create new category")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    public ResponseEntity<?> create(@Valid @RequestBody CategoryCreateDTO categoryCreateDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var newCategory = categoryService.create(categoryCreateDTO);

        if (newCategory != null) {
            return ResponseEntity.status(201).body(newCategory);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category by id")
    @ApiResponse(responseCode = "200", description = "Update category by id")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    public ResponseEntity<?> edit(@PathVariable UUID id, @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var updatedCategoryDTO = categoryService.update(id, categoryDTO);

        if (updatedCategoryDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedCategoryDTO);
    }

    // Create category
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category by id")
    @ApiResponse(responseCode = "200", description = "Delete category by id")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        var existedCategory = categoryService.findById(id);

        if (existedCategory != null) {
            var isDeleted = categoryService.delete(id);
            return ResponseEntity.ok(isDeleted);
        }
        return ResponseEntity.notFound().build();
    }

}
