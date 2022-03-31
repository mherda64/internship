package com.virtuslab.internship.basket.dto;

import com.virtuslab.internship.product.dto.ProductRequestDto;

import java.util.List;

public record BasketRequestDto(
        List<ProductRequestDto> products
) {
}
