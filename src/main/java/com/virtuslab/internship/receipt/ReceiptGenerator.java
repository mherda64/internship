package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ReceiptGenerator {

    public Receipt generate(Basket basket) {
        var products = basket.getProducts();
        var productCount = products.stream()
                .collect(Collectors.toMap(p -> p, p -> 1, Integer::sum));

        var receiptEntries = products.stream()
                .distinct()
                .map(product -> new ReceiptEntry(product, productCount.get(product)))
                .toList();

        return new Receipt(receiptEntries);
    }
}
