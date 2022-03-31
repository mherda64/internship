package com.virtuslab.internship.basket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtuslab.internship.basket.dto.BasketRequestDto;
import com.virtuslab.internship.product.dto.ProductDto;
import com.virtuslab.internship.product.dto.ProductRequestDto;
import com.virtuslab.internship.receipt.dto.ReceiptDto;
import com.virtuslab.internship.receipt.dto.ReceiptEntryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
class BasketControllerTest {

    private final String PRODUCT_1 = "product1";
    private final BigDecimal PRODUCT_1_PRICE = BigDecimal.valueOf(20);
    private final String PRODUCT_2 = "product2";
    private final BigDecimal PRODUCT_2_PRICE = BigDecimal.valueOf(10);
    private final String PRODUCT_3 = "product3";
    private final BigDecimal PRODUCT_3_PRICE = BigDecimal.valueOf(15);

    @MockBean
    private BasketService basketService;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnReceiptDtoGivenBasketDto() throws Exception {
        // Given
        var basketDto = new BasketRequestDto(List.of(
                new ProductRequestDto(PRODUCT_1),
                new ProductRequestDto(PRODUCT_2),
                new ProductRequestDto(PRODUCT_3)
        ));
        var totalPrice = PRODUCT_1_PRICE.add(PRODUCT_2_PRICE).add(PRODUCT_3_PRICE);
        var receiptDto = new ReceiptDto(
                List.of(
                        new ReceiptEntryDto(new ProductDto(PRODUCT_1, PRODUCT_1_PRICE), 1, PRODUCT_1_PRICE),
                        new ReceiptEntryDto(new ProductDto(PRODUCT_2, PRODUCT_2_PRICE), 1, PRODUCT_2_PRICE),
                        new ReceiptEntryDto(new ProductDto(PRODUCT_3, PRODUCT_3_PRICE), 1, PRODUCT_3_PRICE)
                ),
                new ArrayList<>(),
                totalPrice
        );
        when(basketService.generateReceiptWithDiscounts(basketDto)).thenReturn(receiptDto);

        // When
        var result = mockMvc.perform(post("/basket/receipt")
                .content(mapper.writeValueAsString(basketDto))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.entries.length()").value(3))
                .andExpect(jsonPath("$.entries[0].product.name").value(PRODUCT_1))
                .andExpect(jsonPath("$.entries[0].product.price").value(PRODUCT_1_PRICE))
                .andExpect(jsonPath("$.entries[0].quantity").value(1))
                .andExpect(jsonPath("$.entries[0].totalPrice").value(PRODUCT_1_PRICE))
                .andExpect(jsonPath("$.entries[1].product.name").value(PRODUCT_2))
                .andExpect(jsonPath("$.entries[1].product.price").value(PRODUCT_2_PRICE))
                .andExpect(jsonPath("$.entries[1].quantity").value(1))
                .andExpect(jsonPath("$.entries[1].totalPrice").value(PRODUCT_2_PRICE))
                .andExpect(jsonPath("$.entries[2].product.name").value(PRODUCT_3))
                .andExpect(jsonPath("$.entries[2].product.price").value(PRODUCT_3_PRICE))
                .andExpect(jsonPath("$.entries[2].quantity").value(1))
                .andExpect(jsonPath("$.entries[2].totalPrice").value(PRODUCT_3_PRICE))
                .andExpect(jsonPath("$.discounts.length()").value(0))
                .andExpect(jsonPath("$.totalPrice").value(totalPrice));
    }

    @Test
    void shouldReturnReceiptDtoGivenListOfProductNames() throws Exception {
        // Given
        var basketDto = List.of(PRODUCT_1, PRODUCT_2, PRODUCT_3);
        var totalPrice = PRODUCT_1_PRICE.add(PRODUCT_2_PRICE).add(PRODUCT_3_PRICE);
        var receiptDto = new ReceiptDto(
                List.of(
                        new ReceiptEntryDto(new ProductDto(PRODUCT_1, PRODUCT_1_PRICE), 1, PRODUCT_1_PRICE),
                        new ReceiptEntryDto(new ProductDto(PRODUCT_2, PRODUCT_2_PRICE), 1, PRODUCT_2_PRICE),
                        new ReceiptEntryDto(new ProductDto(PRODUCT_3, PRODUCT_3_PRICE), 1, PRODUCT_3_PRICE)
                ),
                new ArrayList<>(),
                totalPrice
        );
        when(basketService.generateReceiptWithDiscounts(any(BasketRequestDto.class))).thenReturn(receiptDto);

        // When
        var result = mockMvc.perform(post("/basket/receipt/names")
                .content(mapper.writeValueAsString(basketDto))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.entries.length()").value(3))
                .andExpect(jsonPath("$.entries[0].product.name").value(PRODUCT_1))
                .andExpect(jsonPath("$.entries[0].product.price").value(PRODUCT_1_PRICE))
                .andExpect(jsonPath("$.entries[0].quantity").value(1))
                .andExpect(jsonPath("$.entries[0].totalPrice").value(PRODUCT_1_PRICE))
                .andExpect(jsonPath("$.entries[1].product.name").value(PRODUCT_2))
                .andExpect(jsonPath("$.entries[1].product.price").value(PRODUCT_2_PRICE))
                .andExpect(jsonPath("$.entries[1].quantity").value(1))
                .andExpect(jsonPath("$.entries[1].totalPrice").value(PRODUCT_2_PRICE))
                .andExpect(jsonPath("$.entries[2].product.name").value(PRODUCT_3))
                .andExpect(jsonPath("$.entries[2].product.price").value(PRODUCT_3_PRICE))
                .andExpect(jsonPath("$.entries[2].quantity").value(1))
                .andExpect(jsonPath("$.entries[2].totalPrice").value(PRODUCT_3_PRICE))
                .andExpect(jsonPath("$.discounts.length()").value(0))
                .andExpect(jsonPath("$.totalPrice").value(totalPrice));
    }

}
