package com.virtuslab.internship.receipt.dto;

import com.virtuslab.internship.discount.dto.DiscountDTO;

import java.math.BigDecimal;
import java.util.List;

public record ReceiptDto(
        List<ReceiptEntryDto> entries,
        List<DiscountDTO> discounts,
        BigDecimal totalPrice
) {
}
