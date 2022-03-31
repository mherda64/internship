package com.virtuslab.internship.discount;

import com.virtuslab.internship.receipt.Receipt;

import java.math.BigDecimal;

public class TenPercentDiscount implements Discount {

    private static final DiscountEnum DISCOUNT = DiscountEnum.TEN_PERCENT_DISCOUNT;
    private static final BigDecimal AMOUNT_REQUIRED = BigDecimal.valueOf(50);

    @Override
    public Receipt apply(Receipt receipt) {
        if (shouldApply(receipt)) {
            var totalPrice = receipt.totalPrice().multiply(BigDecimal.valueOf(0.9));
            var discounts = receipt.discounts();
            discounts.add(DISCOUNT);
            return new Receipt(receipt.entries(), discounts, totalPrice);
        }
        return receipt;
    }

    @Override
    public boolean shouldApply(Receipt receipt) {
        return receipt.totalPrice().compareTo(AMOUNT_REQUIRED) >= 0;
    }

    @Override
    public int getOrder() {
        return DISCOUNT.getOrder();
    }
}
