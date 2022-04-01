package com.virtuslab.internship.configuration;

import com.virtuslab.internship.product.ProductRepository;
import com.virtuslab.internship.product.TestProductDb;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestProductRepositoryConfiguration {

    @Bean
    ProductRepository getProductRepository() {
        return new TestProductDb();
    }
}
