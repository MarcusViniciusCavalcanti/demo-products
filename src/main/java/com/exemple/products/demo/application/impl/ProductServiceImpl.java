package com.exemple.products.demo.application.impl;

import com.exemple.products.demo.application.ProductService;
import com.exemple.products.demo.domain.product.usecase.ProductCreator;
import com.exemple.products.demo.domain.product.usecase.ProductEraser;
import com.exemple.products.demo.domain.product.usecase.ProductGetter;
import com.exemple.products.demo.domain.product.usecase.ProductUpdater;
import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {

    private final ProductCreator productCreator;
    private final ProductUpdater productUpdater;
    private final ProductGetter productGetter;
    private final ProductEraser productEraser;

    @Override
    public ProductDto createNewProduct(InputProduct input) {
        return productCreator.create(input);
    }

    @Override
    public ProductDto update(UUID uuid, InputProduct input) {
        return productUpdater.update(uuid, input);
    }

    @Override
    public List<ProductDto> findAll() {
        return productGetter.findAll();
    }

    @Override
    public List<ProductDto> findAllByFilter(String name, BigDecimal min, BigDecimal max) {
        return productGetter.findAllByFilter(name, min, max);
    }

    @Override
    public ProductDto findById(UUID uuid) {
        return productGetter.findById(uuid);
    }

    @Override
    public void delete(UUID uuid) {
        productEraser.delete(uuid);
    }
}
