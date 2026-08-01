package com.salessavvy.product.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.salessavvy.product.dto.ProductListResponse;
import com.salessavvy.product.dto.ProductResponse;
import com.salessavvy.product.dto.UserSummary;
import com.salessavvy.product.security.AuthenticatedUser;
import com.salessavvy.product.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) { this.productService = productService; }

    @GetMapping
    public ProductListResponse getProducts(@RequestParam(required = false) String category,
            @AuthenticationPrincipal AuthenticatedUser user) {
        return new ProductListResponse(new UserSummary(user.userId(), user.username(), user.role()),
                productService.getProductsByCategory(category));
    }

    @GetMapping("/{productId}")
    public ProductResponse getProduct(@PathVariable Integer productId) {
        return productService.getProduct(productId);
    }
}
