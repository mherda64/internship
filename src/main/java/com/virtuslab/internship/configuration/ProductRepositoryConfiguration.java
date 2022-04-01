package com.virtuslab.internship.configuration;

import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.product.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ProductRepositoryConfiguration {

    @Bean
    @Primary
    ProductRepository getProductRepository() {
        return new ProductDb();
    }

}
