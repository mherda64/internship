package com.virtuslab.internship.basket;

import com.virtuslab.internship.product.Product;

import java.util.ArrayList;
import java.util.List;

public class Basket {

    private final List<Product> products;

    public Basket() {
        products = new ArrayList<>();
    }

    public Basket(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }
}
