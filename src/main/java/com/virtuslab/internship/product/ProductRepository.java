package com.virtuslab.internship.product;

import com.virtuslab.internship.exception.ResourceNotFoundException;

import java.util.List;

public interface ProductRepository {
    Product getProduct(String productName) throws ResourceNotFoundException;

    List<Product> getProducts();
}