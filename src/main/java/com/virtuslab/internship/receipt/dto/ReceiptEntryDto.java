package com.virtuslab.internship.receipt.dto;

import com.virtuslab.internship.product.dto.ProductDto;

import java.math.BigDecimal;

public record ReceiptEntryDto(
        ProductDto product,
        int quantity,
        BigDecimal totalPrice
) {
}
