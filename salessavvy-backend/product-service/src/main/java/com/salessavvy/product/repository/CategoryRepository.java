package com.salessavvy.product.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.salessavvy.product.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);
}
