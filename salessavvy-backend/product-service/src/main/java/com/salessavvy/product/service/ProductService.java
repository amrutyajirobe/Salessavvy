package com.salessavvy.product.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.salessavvy.product.dto.ProductRequest;
import com.salessavvy.product.dto.ProductResponse;
import com.salessavvy.product.entity.Category;
import com.salessavvy.product.entity.Product;
import com.salessavvy.product.entity.ProductImage;
import com.salessavvy.product.exception.ResourceNotFoundException;
import com.salessavvy.product.repository.CategoryRepository;
import com.salessavvy.product.repository.ProductImageRepository;
import com.salessavvy.product.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ProductImageRepository productImageRepository,
            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductResponse> getProductsByCategory(String categoryName) {
        List<Product> products;
        if (categoryName == null || categoryName.isBlank()) {
            products = productRepository.findAll();
        } else {
            Category category = categoryRepository.findByCategoryNameIgnoreCase(categoryName.trim())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryName));
            products = productRepository.findByCategory_CategoryId(category.getCategoryId());
        }
        return toResponses(products);
    }

    public ProductResponse getProduct(Integer productId) {
        Product product = findProduct(productId);
        return toResponse(product, getProductImages(productId));
    }

    public List<String> getProductImages(Integer productId) {
        return productImageRepository.findByProduct_ProductId(productId).stream()
                .map(ProductImage::getImageUrl).toList();
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = findOrCreateCategory(request.category());
        Product product = productRepository.save(new Product(request.name(), request.description(), request.price(), request.stock(), category));
        saveImages(product, request.images());
        return toResponse(product, getProductImages(product.getProductId()));
    }

    @Transactional
    public ProductResponse updateProduct(Integer productId, ProductRequest request) {
        Product product = findProduct(productId);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setCategory(findOrCreateCategory(request.category()));
        productImageRepository.deleteByProduct_ProductId(productId);
        productImageRepository.flush();
        saveImages(product, request.images());
        return toResponse(product, getProductImages(productId));
    }

    @Transactional
    public void deleteProduct(Integer productId) {
        Product product = findProduct(productId);
        productImageRepository.deleteByProduct_ProductId(productId);
        productRepository.delete(product);
    }

    private Category findOrCreateCategory(String name) {
        String normalized = name.trim();
        return categoryRepository.findByCategoryNameIgnoreCase(normalized)
                .orElseGet(() -> categoryRepository.save(new Category(normalized)));
    }

    private Product findProduct(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
    }

    private void saveImages(Product product, Collection<String> urls) {
        if (urls == null || urls.isEmpty()) return;
        productImageRepository.saveAll(urls.stream().map(url -> new ProductImage(url, product)).toList());
    }

    private List<ProductResponse> toResponses(List<Product> products) {
        if (products.isEmpty()) return List.of();
        Map<Integer, List<String>> images = productImageRepository
                .findByProduct_ProductIdIn(products.stream().map(Product::getProductId).toList()).stream()
                .collect(Collectors.groupingBy(image -> image.getProduct().getProductId(),
                        Collectors.mapping(ProductImage::getImageUrl, Collectors.toList())));
        return products.stream().map(product -> toResponse(product,
                images.getOrDefault(product.getProductId(), List.of()))).toList();
    }

    private ProductResponse toResponse(Product product, List<String> images) {
        return new ProductResponse(product.getProductId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getStock(), product.getCategory().getCategoryName(), new ArrayList<>(images));
    }
}
