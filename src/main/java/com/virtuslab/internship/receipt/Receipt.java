package com.virtuslab.internship.receipt;

import com.virtuslab.internship.discount.DiscountEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record Receipt(
        List<ReceiptEntry> entries,
        List<DiscountEnum> discounts,
        BigDecimal totalPrice) {

    public Receipt(List<ReceiptEntry> entries) {
        this(entries,
                new ArrayList<>(),
                entries.stream()
                        .map(ReceiptEntry::totalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }
}
