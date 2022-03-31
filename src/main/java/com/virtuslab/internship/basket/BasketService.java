package com.virtuslab.internship.basket;

import com.virtuslab.internship.basket.dto.BasketRequestDto;
import com.virtuslab.internship.discount.Discount;
import com.virtuslab.internship.discount.FifteenPercentDiscount;
import com.virtuslab.internship.discount.TenPercentDiscount;
import com.virtuslab.internship.exception.ResourceNotFoundException;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.ReceiptGenerator;
import com.virtuslab.internship.receipt.ReceiptMapper;
import com.virtuslab.internship.receipt.dto.ReceiptDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasketService {

    private final ProductDb productDb;
    private final ReceiptGenerator receiptGenerator;
    private final List<Discount> discounts = Stream.of(new TenPercentDiscount(), new FifteenPercentDiscount())
            .sorted(Comparator.comparing(Discount::getOrder))
            .toList();

    public ReceiptDto generateReceiptWithDiscounts(BasketRequestDto basketDTO) {
        Basket basket;
        try {
            basket = new Basket(
                    basketDTO.products().stream()
                            .map(dto -> productDb.getProduct(dto.name()))
                            .toList()
            );
        } catch (ResourceNotFoundException e) {
            log.error("Could not find resource: [{}]", e.getMessage());
            throw e;
        }

        var receipt = receiptGenerator.generate(basket);

        for (var discount : discounts) {
            receipt = discount.apply(receipt);
        }

        return ReceiptMapper.mapToDto(receipt);
    }

}
