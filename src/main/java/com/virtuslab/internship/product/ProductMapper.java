package com.virtuslab.internship.product;

import com.virtuslab.internship.product.dto.ProductDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {

    public static ProductDto mapToDto(Product product) {
        return new ProductDto(product.name(), product.price());
    }

}
