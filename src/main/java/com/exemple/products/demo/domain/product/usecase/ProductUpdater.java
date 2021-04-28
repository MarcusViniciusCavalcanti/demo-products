package com.exemple.products.demo.domain.product.usecase;

import com.exemple.products.demo.domain.product.service.ProductService;
import com.exemple.products.demo.domain.product.service.ProductValidator;
import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductUpdater {

    private final ProductValidator productValidator;
    private final ProductService productService;

    @Transactional
    public ProductDto update(UUID uuid, InputProduct product) {
        productValidator.checkNameExist(uuid, product.getName());
        return productService.updateProduct(uuid, product);
    }
}
