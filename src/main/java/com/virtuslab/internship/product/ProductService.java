package com.virtuslab.internship.product;

import com.virtuslab.internship.product.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDto> getProducts() {
        return productRepository.getProducts().stream()
                .map(ProductMapper::mapToDto)
                .toList();
    }
}
