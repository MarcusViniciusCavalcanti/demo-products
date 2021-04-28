package com.exemple.products.demo.domain.product.service;

import com.exemple.products.demo.domain.product.exception.ProductNameException;
import com.exemple.products.demo.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductValidator {

    private final ProductRepository productRepository;

    public void checkNameExist(String name) {
        var exist = productRepository.existsProductByName(name);

        if (Boolean.FALSE.equals(exist)) {
            throw new ProductNameException(name);
        }
    }

}
