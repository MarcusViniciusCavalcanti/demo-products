package com.exemple.products.demo.domain.product.service;

import com.exemple.products.demo.domain.product.exception.ProductNameException;
import com.exemple.products.demo.domain.product.repository.ProductRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductValidator {

    private final ProductRepository productRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public void checkNameExist(String name) {
        var exist = productRepository.existsProductByName(name);

        if (Boolean.TRUE.equals(exist)) {
            throw new ProductNameException(name);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void checkNameExist(UUID uuid, String name) {
        var exist = productRepository.existsProductByNameAndUuidNot(name, uuid);

        if (Boolean.TRUE.equals(exist)) {
            throw new ProductNameException(name);
        }
    }
}
