package com.exemple.products.demo.domain.product.service;

import com.exemple.products.demo.domain.product.entity.Product;
import com.exemple.products.demo.domain.product.repository.ProductRepository;
import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public ProductDto saveNewProduct(InputProduct inputProduct) {
        var productSaved = productRepository.save(replace(new Product(), inputProduct));
        return buildDto(productSaved);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public ProductDto updateProduct(UUID uuid, InputProduct inputProduct) {
        var product = getProductById(uuid);
        var productSaved = productRepository.save(replace(product, inputProduct));
        return buildDto(productSaved);
    }

    public ProductDto findById(UUID uuid) {
        return buildDto(getProductById(uuid));
    }

    private Product getProductById(UUID uuid) {
        return productRepository.findById(uuid)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public List<ProductDto> findAll() {
        return getDtoList(productRepository.findAll());
    }

    public List<ProductDto> findAll(@NonNull String nameFilter, @NonNull BigDecimal minFilter, @NonNull BigDecimal maxFilter) {
        var spec = SpecificationFilter.buildFilter(nameFilter, minFilter, maxFilter);
        return getDtoList(productRepository.findAll(spec));
    }

    private List<ProductDto> getDtoList(List<Product> all) {
        return all.stream()
            .map(product -> ProductDto.builder()
                .id(product.getUuid())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build())
            .collect(Collectors.toList());
    }

    private ProductDto buildDto(Product product) {
        return ProductDto.builder()
            .id(product.getUuid())
            .description(product.getDescription())
            .price(product.getPrice())
            .name(product.getName())
            .build();
    }

    private Product replace(Product product, InputProduct inputProduct) {
        var price = getPriceConverted(inputProduct);
        product.setPrice(price);
        product.setName(inputProduct.getName());
        product.setDescription(inputProduct.getDescription());

        return product;
    }

    private BigDecimal getPriceConverted(InputProduct inputProduct) {
        var price = inputProduct.getPrice();
        return price.setScale(3, RoundingMode.HALF_EVEN);
    }
}
