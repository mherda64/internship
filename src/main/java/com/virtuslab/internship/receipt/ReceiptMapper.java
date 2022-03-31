package com.virtuslab.internship.receipt;

import com.virtuslab.internship.discount.DiscountMapper;
import com.virtuslab.internship.product.ProductMapper;
import com.virtuslab.internship.receipt.dto.ReceiptDto;
import com.virtuslab.internship.receipt.dto.ReceiptEntryDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReceiptMapper {

    public static ReceiptEntryDto mapToDto(ReceiptEntry receiptEntry) {
        return new ReceiptEntryDto(
                ProductMapper.mapToDto(receiptEntry.product()),
                receiptEntry.quantity(),
                receiptEntry.totalPrice()
        );
    }

    public static ReceiptDto mapToDto(Receipt receipt) {
        return new ReceiptDto(
                receipt.entries().stream()
                        .map(ReceiptMapper::mapToDto)
                        .toList(),
                receipt.discounts().stream()
                        .map(DiscountMapper::mapToDto)
                        .toList(),
                receipt.totalPrice()
        );
    }

}
