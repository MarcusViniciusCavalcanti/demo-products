package com.exemple.products.demo.application;

import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

    ProductDto createNewProduct(InputProduct input);

    ProductDto update(UUID uuid, InputProduct input);

    List<ProductDto> findAll();

    List<ProductDto> findAllByFilter(String name, BigDecimal min, BigDecimal max);

    ProductDto findById(UUID uuid);

    void delete(UUID uuid);
}
