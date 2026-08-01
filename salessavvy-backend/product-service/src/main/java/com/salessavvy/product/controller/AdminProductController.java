package com.salessavvy.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.salessavvy.product.dto.ProductRequest;
import com.salessavvy.product.dto.ProductResponse;
import com.salessavvy.product.service.ProductService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {
    private final ProductService productService;
    public AdminProductController(ProductService productService) { this.productService = productService; }

    @PostMapping
    ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }
    @PutMapping("/{productId}")
    ProductResponse update(@PathVariable Integer productId, @Valid @RequestBody ProductRequest request) {
        return productService.updateProduct(productId, request);
    }
    @DeleteMapping("/{productId}")
    ResponseEntity<Void> delete(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
