package com.virtuslab.internship.discount;

import lombok.Getter;

@Getter
public enum DiscountEnum {

    FIFTEEN_PERCENT_DISCOUNT("FifteenPercentDiscountOnGrains", 10),
    TEN_PERCENT_DISCOUNT("TenPercentDiscount", 20);

    private final String name;
    private final int order;

    DiscountEnum(String name, int order) {
        this.name = name;
        this.order = order;
    }
}
