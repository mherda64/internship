package com.virtuslab.internship.basket;

import com.virtuslab.internship.basket.dto.BasketRequestDto;
import com.virtuslab.internship.product.dto.ProductRequestDto;
import com.virtuslab.internship.receipt.dto.ReceiptDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BasketController {

    private final BasketService basketService;

    @PostMapping("basket/receipt")
    public ReceiptDto generateReceipt(@RequestBody BasketRequestDto basketDTO) {
        log.info("Received basket [{}]", basketDTO);
        var receipt = basketService.generateReceiptWithDiscounts(basketDTO);
        log.info("Returning calculated receipt [{}]", receipt);
        return receipt;
    }

    @PostMapping("basket/receipt/names")
    public ReceiptDto generateReceipt(@RequestBody List<String> productNamesList) {
        log.info("Received list of products [{}]", productNamesList);
        var basketDTO = new BasketRequestDto(
                productNamesList.stream()
                        .map(ProductRequestDto::new)
                        .toList()
        );
        var receipt = basketService.generateReceiptWithDiscounts(basketDTO);
        log.info("Returning calculated receipt [{}]", receipt);
        return receipt;
    }

}
