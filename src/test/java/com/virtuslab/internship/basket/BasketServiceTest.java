package com.virtuslab.internship.basket;

import com.virtuslab.internship.basket.dto.BasketRequestDto;
import com.virtuslab.internship.discount.FifteenPercentDiscount;
import com.virtuslab.internship.discount.TenPercentDiscount;
import com.virtuslab.internship.exception.ResourceNotFoundException;
import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.product.dto.ProductRequestDto;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import com.virtuslab.internship.receipt.ReceiptGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    private final Product GRAIN_PRODUCT = new Product("GrainProduct", Product.Type.GRAINS, BigDecimal.valueOf(10));
    private final Product DAIRY_PRODUCT = new Product("DairyProduct", Product.Type.DAIRY, BigDecimal.valueOf(20));
    private final Product FRUIT_PRODUCT = new Product("FruitProduct", Product.Type.FRUITS, BigDecimal.valueOf(5));
    private final Product MEAT_PRODUCT = new Product("MeatProduct", Product.Type.MEAT, BigDecimal.valueOf(30));

    @Mock
    private ProductDb productDb;

    @Mock
    private ReceiptGenerator receiptGenerator;

    @InjectMocks
    private BasketService basketService;

    @Test
    void shouldGenerateReceiptWithoutDiscounts() {
        // Given
        var basketRequest = new BasketRequestDto(List.of(
                new ProductRequestDto(GRAIN_PRODUCT.name()),
                new ProductRequestDto(DAIRY_PRODUCT.name())
        ));
        var generated = new Receipt(
                List.of(
                        new ReceiptEntry(GRAIN_PRODUCT, 1, GRAIN_PRODUCT.price()),
                        new ReceiptEntry(DAIRY_PRODUCT, 1, DAIRY_PRODUCT.price())
                )
        );
        var expected = new Receipt(
                List.of(
                        new ReceiptEntry(GRAIN_PRODUCT, 1, GRAIN_PRODUCT.price()),
                        new ReceiptEntry(DAIRY_PRODUCT, 1, DAIRY_PRODUCT.price())
                )
        );

        when(productDb.getProduct(GRAIN_PRODUCT.name())).thenReturn(GRAIN_PRODUCT);
        when(productDb.getProduct(DAIRY_PRODUCT.name())).thenReturn(DAIRY_PRODUCT);
        when(receiptGenerator.generate(any(Basket.class))).thenReturn(generated);

        // When
        var outputReceipt = basketService.generateReceiptWithDiscounts(basketRequest);

        // Then
        assertEquals(expected.entries().size(), outputReceipt.entries().size());
        assertEquals(expected.totalPrice(), outputReceipt.totalPrice());
        assertEquals(expected.discounts().size(), outputReceipt.discounts().size());

        assertEquals(expected.entries().get(0).quantity(), outputReceipt.entries().get(0).quantity());
        assertEquals(expected.entries().get(0).totalPrice(), outputReceipt.entries().get(0).totalPrice());
        assertEquals(GRAIN_PRODUCT.name(), outputReceipt.entries().get(0).product().name());
        assertEquals(GRAIN_PRODUCT.price(), outputReceipt.entries().get(0).product().price());

        assertEquals(expected.entries().get(1).quantity(), outputReceipt.entries().get(1).quantity());
        assertEquals(expected.entries().get(1).totalPrice(), outputReceipt.entries().get(1).totalPrice());
        assertEquals(DAIRY_PRODUCT.name(), outputReceipt.entries().get(1).product().name());
        assertEquals(DAIRY_PRODUCT.price(), outputReceipt.entries().get(1).product().price());
    }

    @Test
    void shouldGenerateReceiptWith10PercentDiscount() {
        // Given
        var basketRequest = new BasketRequestDto(List.of(
                new ProductRequestDto(GRAIN_PRODUCT.name()),
                new ProductRequestDto(GRAIN_PRODUCT.name()),
                new ProductRequestDto(GRAIN_PRODUCT.name()),
                new ProductRequestDto(FRUIT_PRODUCT.name())
        ));
        var generated = new Receipt(
                List.of(
                        new ReceiptEntry(GRAIN_PRODUCT, 3, GRAIN_PRODUCT.price().multiply(BigDecimal.valueOf(3))),
                        new ReceiptEntry(FRUIT_PRODUCT, 1, GRAIN_PRODUCT.price())
                )
        );
        var expected = new Receipt(
                List.of(
                        new ReceiptEntry(GRAIN_PRODUCT, 3, GRAIN_PRODUCT.price().multiply(BigDecimal.valueOf(3))),
                        new ReceiptEntry(FRUIT_PRODUCT, 1, GRAIN_PRODUCT.price())
                )
        );
        expected = new FifteenPercentDiscount().apply(expected);

        when(productDb.getProduct(GRAIN_PRODUCT.name())).thenReturn(GRAIN_PRODUCT);
        when(productDb.getProduct(FRUIT_PRODUCT.name())).thenReturn(FRUIT_PRODUCT);
        when(receiptGenerator.generate(any(Basket.class))).thenReturn(generated);

        // When
        var outputReceipt = basketService.generateReceiptWithDiscounts(basketRequest);

        // Then
        assertEquals(expected.entries().size(), outputReceipt.entries().size());
        assertEquals(expected.totalPrice(), outputReceipt.totalPrice());
        assertEquals(expected.discounts().size(), outputReceipt.discounts().size());

        assertEquals(expected.entries().get(0).quantity(), outputReceipt.entries().get(0).quantity());
        assertEquals(expected.entries().get(0).totalPrice(), outputReceipt.entries().get(0).totalPrice());
        assertEquals(GRAIN_PRODUCT.name(), outputReceipt.entries().get(0).product().name());
        assertEquals(GRAIN_PRODUCT.price(), outputReceipt.entries().get(0).product().price());

        assertEquals(expected.entries().get(1).quantity(), outputReceipt.entries().get(1).quantity());
        assertEquals(expected.entries().get(1).totalPrice(), outputReceipt.entries().get(1).totalPrice());
        assertEquals(FRUIT_PRODUCT.name(), outputReceipt.entries().get(1).product().name());
        assertEquals(FRUIT_PRODUCT.price(), outputReceipt.entries().get(1).product().price());
    }

    @Test
    void shouldGenerate15PercentAnd10PercentDiscounts() {
        // Given
        var basketRequest = new BasketRequestDto(List.of(
                new ProductRequestDto(GRAIN_PRODUCT.name()),
                new ProductRequestDto(GRAIN_PRODUCT.name()),
                new ProductRequestDto(GRAIN_PRODUCT.name()),
                new ProductRequestDto(FRUIT_PRODUCT.name()),
                new ProductRequestDto(MEAT_PRODUCT.name())
        ));
        var generated = new Receipt(
                List.of(
                        new ReceiptEntry(GRAIN_PRODUCT, 3, GRAIN_PRODUCT.price().multiply(BigDecimal.valueOf(3))),
                        new ReceiptEntry(FRUIT_PRODUCT, 1, GRAIN_PRODUCT.price()),
                        new ReceiptEntry(MEAT_PRODUCT, 1, MEAT_PRODUCT.price())
                )
        );
        var expected = new Receipt(
                List.of(
                        new ReceiptEntry(GRAIN_PRODUCT, 3, GRAIN_PRODUCT.price().multiply(BigDecimal.valueOf(3))),
                        new ReceiptEntry(FRUIT_PRODUCT, 1, GRAIN_PRODUCT.price()),
                        new ReceiptEntry(MEAT_PRODUCT, 1, MEAT_PRODUCT.price())
                )
        );
        expected = new FifteenPercentDiscount().apply(expected);
        expected = new TenPercentDiscount().apply(expected);

        when(productDb.getProduct(GRAIN_PRODUCT.name())).thenReturn(GRAIN_PRODUCT);
        when(productDb.getProduct(FRUIT_PRODUCT.name())).thenReturn(FRUIT_PRODUCT);
        when(productDb.getProduct(MEAT_PRODUCT.name())).thenReturn(MEAT_PRODUCT);
        when(receiptGenerator.generate(any(Basket.class))).thenReturn(generated);

        // When
        var outputReceipt = basketService.generateReceiptWithDiscounts(basketRequest);

        // Then
        assertEquals(expected.entries().size(), outputReceipt.entries().size());
        assertEquals(expected.totalPrice(), outputReceipt.totalPrice());
        assertEquals(expected.discounts().size(), outputReceipt.discounts().size());

        assertEquals(expected.entries().get(0).quantity(), outputReceipt.entries().get(0).quantity());
        assertEquals(expected.entries().get(0).totalPrice(), outputReceipt.entries().get(0).totalPrice());
        assertEquals(GRAIN_PRODUCT.name(), outputReceipt.entries().get(0).product().name());
        assertEquals(GRAIN_PRODUCT.price(), outputReceipt.entries().get(0).product().price());

        assertEquals(expected.entries().get(1).quantity(), outputReceipt.entries().get(1).quantity());
        assertEquals(expected.entries().get(1).totalPrice(), outputReceipt.entries().get(1).totalPrice());
        assertEquals(FRUIT_PRODUCT.name(), outputReceipt.entries().get(1).product().name());
        assertEquals(FRUIT_PRODUCT.price(), outputReceipt.entries().get(1).product().price());

        assertEquals(expected.entries().get(2).quantity(), outputReceipt.entries().get(2).quantity());
        assertEquals(expected.entries().get(2).totalPrice(), outputReceipt.entries().get(2).totalPrice());
        assertEquals(MEAT_PRODUCT.name(), outputReceipt.entries().get(2).product().name());
        assertEquals(MEAT_PRODUCT.price(), outputReceipt.entries().get(2).product().price());
    }

    @Test
    void shouldGenerateReceiptWith15PercentDiscount() {
        // Given
        var basketRequest = new BasketRequestDto(List.of(
                new ProductRequestDto(MEAT_PRODUCT.name()),
                new ProductRequestDto(MEAT_PRODUCT.name())
        ));
        var generated = new Receipt(
                List.of(
                        new ReceiptEntry(MEAT_PRODUCT, 2, MEAT_PRODUCT.price().multiply(BigDecimal.valueOf(2)))
                )
        );
        var expected = new Receipt(
                List.of(
                        new ReceiptEntry(MEAT_PRODUCT, 2, MEAT_PRODUCT.price().multiply(BigDecimal.valueOf(2)))
                )
        );
        expected = new TenPercentDiscount().apply(expected);

        when(productDb.getProduct(MEAT_PRODUCT.name())).thenReturn(MEAT_PRODUCT);
        when(receiptGenerator.generate(any(Basket.class))).thenReturn(generated);

        // When
        var outputReceipt = basketService.generateReceiptWithDiscounts(basketRequest);

        // Then
        assertEquals(expected.entries().size(), outputReceipt.entries().size());
        assertEquals(expected.totalPrice(), outputReceipt.totalPrice());
        assertEquals(expected.discounts().size(), outputReceipt.discounts().size());

        assertEquals(expected.entries().get(0).quantity(), outputReceipt.entries().get(0).quantity());
        assertEquals(expected.entries().get(0).totalPrice(), outputReceipt.entries().get(0).totalPrice());
        assertEquals(MEAT_PRODUCT.name(), outputReceipt.entries().get(0).product().name());
        assertEquals(MEAT_PRODUCT.price(), outputReceipt.entries().get(0).product().price());
    }


    @Test
    void shouldThrowOnUnknownProduct() {
        // Given
        var unknownProductName = "UnknownProduct";
        var basketRequest = new BasketRequestDto(List.of(
                new ProductRequestDto(GRAIN_PRODUCT.name()),
                new ProductRequestDto(unknownProductName)
        ));

        when(productDb.getProduct(GRAIN_PRODUCT.name())).thenReturn(GRAIN_PRODUCT);
        when(productDb.getProduct(unknownProductName)).thenThrow(new ResourceNotFoundException(""));

        // When
        // Then
        assertThrows(ResourceNotFoundException.class, () -> basketService.generateReceiptWithDiscounts(basketRequest));
    }

}
