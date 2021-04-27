package com.exemple.products.demo.rest.endpoints;

import com.exemple.products.demo.rest.exception.IllegalRequestBodyException;
import com.exemple.products.demo.rest.inputs.InputProduct;
import com.exemple.products.demo.structure.dto.ProductDto;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductsController {


    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody InputProduct input, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalRequestBodyException("product", result);
        }

        return ResponseEntity.ok().build();
    }
}
