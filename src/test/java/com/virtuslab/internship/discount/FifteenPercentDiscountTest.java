package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FifteenPercentDiscountTest {

    private static final String CHEESE = "Cheese";
    private static final String STEAK = "Steak";
    private static final String BREAD = "Bread";
    private static final String CEREALS = "Cereals";

    @Test
    void shouldApply15PercentDiscountWhenAtLeast3GrainProducts() {
        // Given
        var productDb = new ProductDb();
        var bread = productDb.getProduct(BREAD);
        var cereals = productDb.getProduct(CEREALS);
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(bread, 2));
        receiptEntries.add(new ReceiptEntry(cereals, 2));

        var receipt = new Receipt(receiptEntries);
        var discount = new FifteenPercentDiscount();
        var expectedTotalPrice = bread.price().multiply(BigDecimal.valueOf(2))
                .add(cereals.price().multiply(BigDecimal.valueOf(2))).multiply(BigDecimal.valueOf(0.85));

        // When
        var receiptAfterDiscount = discount.apply(receipt);

        //Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(1, receiptAfterDiscount.discounts().size());
    }

    @Test
    void shouldApply15PercentDiscountWhenExactly3GrainProducts() {
        // Given
        var productDb = new ProductDb();
        var bread = productDb.getProduct(BREAD);
        var cereals = productDb.getProduct(CEREALS);
        var cheese = productDb.getProduct(CHEESE);
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(bread, 2));
        receiptEntries.add(new ReceiptEntry(cereals, 1));
        receiptEntries.add(new ReceiptEntry(cheese, 1));

        var receipt = new Receipt(receiptEntries);
        var discount = new FifteenPercentDiscount();
        var expectedTotalPrice = bread.price().multiply(BigDecimal.valueOf(2))
                .add(cereals.price()).add(cheese.price()).multiply(BigDecimal.valueOf(0.85));

        // When
        var receiptAfterDiscount = discount.apply(receipt);

        //Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(1, receiptAfterDiscount.discounts().size());
    }

    @Test
    void shouldNotApply15PercentDiscountWhenLessThan3GrainProducts() {
        // Given
        var productDb = new ProductDb();
        var cheese = productDb.getProduct(CHEESE);
        var steak = productDb.getProduct(STEAK);
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cheese, 2));
        receiptEntries.add(new ReceiptEntry(steak, 1));

        var receipt = new Receipt(receiptEntries);
        var discount = new FifteenPercentDiscount();
        var expectedTotalPrice = cheese.price().multiply(BigDecimal.valueOf(2)).add(steak.price());

        // When
        var receiptAfterDiscount = discount.apply(receipt);

        //Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(0, receiptAfterDiscount.discounts().size());
    }

}
