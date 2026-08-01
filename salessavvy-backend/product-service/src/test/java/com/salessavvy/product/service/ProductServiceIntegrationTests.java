package com.salessavvy.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.salessavvy.product.dto.ProductResponse;
import com.salessavvy.product.entity.Category;
import com.salessavvy.product.entity.Product;
import com.salessavvy.product.entity.ProductImage;
import com.salessavvy.product.exception.ResourceNotFoundException;
import com.salessavvy.product.repository.CategoryRepository;
import com.salessavvy.product.repository.ProductImageRepository;
import com.salessavvy.product.repository.ProductRepository;

@SpringBootTest
@Transactional
class ProductServiceIntegrationTests {
    @Autowired ProductService productService;
    @Autowired ProductRepository productRepository;
    @Autowired ProductImageRepository imageRepository;
    @Autowired CategoryRepository categoryRepository;

    @BeforeEach
    void seedProducts() {
        Category electronics = categoryRepository.save(new Category("Electronics"));
        Category books = categoryRepository.save(new Category("Books"));
        Product phone = productRepository.save(new Product("Phone", "5G phone", new BigDecimal("499.99"), 8, electronics));
        productRepository.save(new Product("Novel", "Paperback", new BigDecimal("14.50"), 20, books));
        imageRepository.saveAll(List.of(new ProductImage("phone-front.jpg", phone), new ProductImage("phone-back.jpg", phone)));
    }

    @Test
    void returnsAllProductsWithTheirImages() {
        List<ProductResponse> products = productService.getProductsByCategory(null);
        assertThat(products).hasSize(2);
        assertThat(products).filteredOn(product -> product.name().equals("Phone"))
                .singleElement().extracting(ProductResponse::images)
                .asList().containsExactlyInAnyOrder("phone-front.jpg", "phone-back.jpg");
    }

    @Test
    void filtersCategoryWithoutCaseSensitivity() {
        assertThat(productService.getProductsByCategory(" electronics "))
                .singleElement().extracting(ProductResponse::name).isEqualTo("Phone");
    }

    @Test
    void reportsAnUnknownCategory() {
        assertThatThrownBy(() -> productService.getProductsByCategory("Missing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found: Missing");
    }
}
