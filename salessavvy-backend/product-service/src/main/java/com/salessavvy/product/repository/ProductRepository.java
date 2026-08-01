package com.salessavvy.product.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.salessavvy.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategory_CategoryId(Integer categoryId);
}
