package com.virtuslab.internship.basket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtuslab.internship.basket.dto.BasketRequestDto;
import com.virtuslab.internship.discount.DiscountEnum;
import com.virtuslab.internship.discount.TenPercentDiscount;
import com.virtuslab.internship.product.ProductRepository;
import com.virtuslab.internship.product.dto.ProductRequestDto;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BasketControllerIntegrationTest {

    private static final String CHEESE = "Cheese";
    private static final String STEAK = "Steak";

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repository;

    @Test
    void shouldReturnReceiptDtoGivenBasketDto() throws Exception {
        var cheeseProduct = repository.getProduct(CHEESE);
        var steakProduct = repository.getProduct(STEAK);
        // Given
        var basketDto = new BasketRequestDto(List.of(
                new ProductRequestDto(CHEESE),
                new ProductRequestDto(STEAK)
        ));
        var expected = new TenPercentDiscount().apply(
                new Receipt(List.of(
                        new ReceiptEntry(cheeseProduct, 1),
                        new ReceiptEntry(steakProduct, 1)
                ))
        );

        // When
        var result = mockMvc.perform(post("/basket/receipt")
                .content(mapper.writeValueAsString(basketDto))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.entries.length()").value(2))
                .andExpect(jsonPath("$.entries[0].product.name").value(cheeseProduct.name()))
                .andExpect(jsonPath("$.entries[0].product.price").value(cheeseProduct.price()))
                .andExpect(jsonPath("$.entries[0].quantity").value(1))
                .andExpect(jsonPath("$.entries[0].totalPrice").value(cheeseProduct.price()))
                .andExpect(jsonPath("$.entries[1].product.name").value(steakProduct.name()))
                .andExpect(jsonPath("$.entries[1].product.price").value(steakProduct.price()))
                .andExpect(jsonPath("$.entries[1].quantity").value(1))
                .andExpect(jsonPath("$.entries[1].totalPrice").value(steakProduct.price()))
                .andExpect(jsonPath("$.discounts.length()").value(1))
                .andExpect(jsonPath("$.discounts[0].name").value(DiscountEnum.TEN_PERCENT_DISCOUNT.getName()))
                .andExpect(jsonPath("$.totalPrice").value(expected.totalPrice()));
    }

    @Test
    void shouldReturnReceiptDtoGivenListOfProductNames() throws Exception {
        var cheeseProduct = repository.getProduct(CHEESE);
        // Given
        var nameList = List.of(cheeseProduct.name(), cheeseProduct.name());
        var expected = new Receipt(List.of(
                new ReceiptEntry(cheeseProduct, 2)
        ));

        // When
        var result = mockMvc.perform(post("/basket/receipt/names")
                .content(mapper.writeValueAsString(nameList))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.entries.length()").value(1))
                .andExpect(jsonPath("$.entries[0].product.name").value(cheeseProduct.name()))
                .andExpect(jsonPath("$.entries[0].product.price").value(cheeseProduct.price()))
                .andExpect(jsonPath("$.entries[0].quantity").value(2))
                .andExpect(jsonPath("$.entries[0].totalPrice").value(cheeseProduct.price().multiply(BigDecimal.valueOf(2))))
                .andExpect(jsonPath("$.discounts.length()").value(0))
                .andExpect(jsonPath("$.totalPrice").value(expected.totalPrice()));
    }

}
