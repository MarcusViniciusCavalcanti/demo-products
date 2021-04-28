package com.exemple.products.demo.application;

import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

    ProductDto createNewProduct(InputProduct input);

}
