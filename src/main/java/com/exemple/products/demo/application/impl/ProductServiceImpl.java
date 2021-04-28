package com.exemple.products.demo.application.impl;

import com.exemple.products.demo.application.ProductService;
import com.exemple.products.demo.domain.product.usecase.ProductCreator;
import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {

    private final ProductCreator productCreator;

    @Override
    public ProductDto createNewProduct(InputProduct input) {
        return productCreator.create(input);
    }
}
