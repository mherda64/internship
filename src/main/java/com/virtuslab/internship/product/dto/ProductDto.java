package com.virtuslab.internship.product.dto;

import java.math.BigDecimal;

public record ProductDto(
        String name,
        BigDecimal price
) {
}
