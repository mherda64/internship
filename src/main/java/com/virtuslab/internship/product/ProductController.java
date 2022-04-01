package com.virtuslab.internship.product;

import com.virtuslab.internship.product.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping("products")
    public List<ProductDto> getProducts() {
        var products = productService.getProducts();
        log.info("Returning products list [{}]", products);
        return products;
    }

}
