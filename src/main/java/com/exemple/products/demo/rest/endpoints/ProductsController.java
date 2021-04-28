package com.exemple.products.demo.rest.endpoints;

import com.exemple.products.demo.application.ProductService;
import com.exemple.products.demo.rest.exception.IllegalRequestBodyException;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import com.exemple.products.demo.structure.dto.ProductDto;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductsController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody InputProduct input) {
        var dto = productService.createNewProduct(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(
        @Valid @RequestBody InputProduct inputProduct,
        @PathVariable("id") UUID id) {

        return null;
    }


}
