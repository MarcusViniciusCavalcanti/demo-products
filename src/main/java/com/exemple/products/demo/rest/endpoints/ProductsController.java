package com.exemple.products.demo.rest.endpoints;

import com.exemple.products.demo.application.ProductService;
import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import com.exemple.products.demo.structure.dto.inputs.ParamsSearch;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
        var dto = productService.update(id, inputProduct);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll() {
        var products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> findAllByFilter(ParamsSearch paramsSearch) {
        var min = Objects.nonNull(paramsSearch.getMin_price()) ?
            BigDecimal.valueOf(paramsSearch.getMin_price()) : null;
        var max = Objects.nonNull(paramsSearch.getMax_price()) ?
            BigDecimal.valueOf(paramsSearch.getMax_price()) : null;

        var products = productService.findAllByFilter(paramsSearch.getQ(), min, max);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable("id") UUID id) {
        var dto = productService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }

}
