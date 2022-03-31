package com.virtuslab.internship.discount;

import com.virtuslab.internship.discount.dto.DiscountDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiscountMapper {
    public static DiscountDTO mapToDto(DiscountEnum discountEnum) {
        return new DiscountDTO(discountEnum.getName());
    }
}
