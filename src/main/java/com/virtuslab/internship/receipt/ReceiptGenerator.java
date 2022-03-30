package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.Product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReceiptGenerator {

    public Receipt generate(Basket basket) {
        Map<Product, Integer> productCount = basket.getProducts().stream()
                .collect(Collectors.toMap(p -> p, p -> 1, Integer::sum));
        List<ReceiptEntry> receiptEntries = basket.getProducts().stream()
                .distinct()
                .map(product -> new ReceiptEntry(product, productCount.get(product)))
                .toList();
        return new Receipt(receiptEntries);
    }
}
