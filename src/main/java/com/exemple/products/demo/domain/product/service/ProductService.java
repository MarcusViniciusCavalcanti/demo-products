package com.exemple.products.demo.domain.product.service;

import com.exemple.products.demo.domain.product.entity.Product;
import com.exemple.products.demo.domain.product.exception.ProductNameException;
import com.exemple.products.demo.domain.product.repository.ProductRepository;
import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public ProductDto saveNewProduct(InputProduct inputProduct) {
        var productSaved = productRepository.save(buildBy(inputProduct));
        return ProductDto.builder()
            .id(productSaved.getUuid())
            .description(productSaved.getDescription())
            .price(productSaved.getPrice())
            .name(productSaved.getName())
            .build();
    }

    private Product buildBy(InputProduct inputProduct) {
        var product = new Product();

        var price = getPriceConverted(inputProduct);
        product.setPrice(price);
        product.setName(inputProduct.getName());
        product.setDescription(inputProduct.getDescription());

        return product;
    }

    private BigDecimal getPriceConverted(InputProduct inputProduct) {
        var price = inputProduct.getPrice();
        return price.setScale(3, RoundingMode.HALF_EVEN);
    }
}
