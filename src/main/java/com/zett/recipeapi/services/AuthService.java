package com.zett.recipeapi.services;

public interface AuthService {
    boolean existsByUsername(String username); // Check if username exists
}