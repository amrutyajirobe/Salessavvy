package com.salessavvy.product.dto;
import java.util.List;
public record ProductListResponse(UserSummary user, List<ProductResponse> products) {}
