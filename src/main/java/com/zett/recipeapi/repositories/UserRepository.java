package com.zett.recipeapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zett.recipeapi.entities.User;

public interface UserRepository extends JpaRepository <User, UUID>, JpaSpecificationExecutor<User> {
    User findByUsername(String username);
    User findByUsernameOrEmail(String username, String email);
    boolean existsByUsername(String username);
}
