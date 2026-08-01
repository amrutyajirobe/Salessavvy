package com.salessavvy.product.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(Integer productId, String name, String description, BigDecimal price,
        Integer stock, String category, List<String> images) {}
