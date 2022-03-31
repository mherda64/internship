package com.virtuslab.internship.product.dto;

import javax.validation.constraints.NotNull;

public record ProductRequestDto(
        @NotNull String name
) {
}
