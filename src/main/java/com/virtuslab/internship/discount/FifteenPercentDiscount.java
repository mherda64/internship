package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;

import java.math.BigDecimal;

public class FifteenPercentDiscount implements Discount {

    private static final DiscountEnum DISCOUNT = DiscountEnum.FIFTEEN_PERCENT_DISCOUNT;
    private static final int AMOUNT_REQUIRED = 3;

    @Override
    public Receipt apply(Receipt receipt) {
        if (shouldApply(receipt)) {
            var totalPrice = receipt.totalPrice().multiply(BigDecimal.valueOf(0.85));
            var discounts = receipt.discounts();
            discounts.add(DISCOUNT);
            return new Receipt(receipt.entries(), discounts, totalPrice);
        }
        return receipt;
    }

    @Override
    public boolean shouldApply(Receipt receipt) {
        return receipt.entries().stream()
                .filter(entry -> entry.product().type().equals(Product.Type.GRAINS))
                .map(ReceiptEntry::quantity)
                .reduce(Integer::sum)
                .orElse(0) >= AMOUNT_REQUIRED;
    }

    @Override
    public int getOrder() {
        return DISCOUNT.getOrder();
    }
}
