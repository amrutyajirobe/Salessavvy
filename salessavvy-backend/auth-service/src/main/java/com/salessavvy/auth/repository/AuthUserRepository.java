package com.salessavvy.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salessavvy.auth.entity.AuthUser;

@Repository
public interface AuthUserRepository
        extends JpaRepository<AuthUser, Integer> {

    Optional<AuthUser> findByEmail(String email);

    Optional<AuthUser> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
