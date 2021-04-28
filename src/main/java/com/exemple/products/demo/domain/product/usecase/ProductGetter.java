package com.exemple.products.demo.domain.product.usecase;

import com.exemple.products.demo.domain.product.service.ProductService;
import com.exemple.products.demo.structure.dto.ProductDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductGetter {

    private final ProductService productService;

    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    public List<ProductDto> findAllByFilter(String name, BigDecimal min, BigDecimal max) {
        var nameFilter = Objects.isNull(name) ? "" : name;
        var minFilter = Objects.isNull(min) ? BigDecimal.ZERO : min;
        var maxFilter = Objects.isNull(max) ? BigDecimal.valueOf(Long.MAX_VALUE, 3) : max;

        return productService.findAll(nameFilter, minFilter, maxFilter);
    }

    public ProductDto findById(UUID uuid) {
        return productService.findById(uuid);
    }
}
