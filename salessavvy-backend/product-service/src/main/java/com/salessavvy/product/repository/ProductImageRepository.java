package com.salessavvy.product.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.salessavvy.product.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByProduct_ProductId(Integer productId);
    List<ProductImage> findByProduct_ProductIdIn(Collection<Integer> productIds);
    void deleteByProduct_ProductId(Integer productId);
}
