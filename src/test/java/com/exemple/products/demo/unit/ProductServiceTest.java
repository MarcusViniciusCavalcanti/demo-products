package com.exemple.products.demo.unit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.exemple.products.demo.domain.product.entity.Product;
import com.exemple.products.demo.domain.product.repository.ProductRepository;
import com.exemple.products.demo.domain.product.service.ProductService;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;


@DisplayName("Unit - Test ProductService")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Captor
    private ArgumentCaptor<Product> argCaptor;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("should return product with round price and scale three decimal")
    void shouldReturnProductWithRoundPriceAndScale() {
        when(productRepository.save(any(Product.class))).thenReturn(new Product());

        var input =
            new InputProduct("Product Name", "Product Description",
                BigDecimal.valueOf(90.00323422222));

        productService.saveNewProduct(input);

        verify(productRepository).save(argCaptor.capture());
        var product = argCaptor.getValue();
        assertEquals(BigDecimal.valueOf(90.003), product.getPrice());
    }

    @Test
    @DisplayName("should return product saved")
    void shouldReturnProductSaved() {
        when(productRepository.save(any(Product.class))).thenReturn(new Product());

        var input = new InputProduct(
            "Product Name",
            "Product Description",
                BigDecimal.valueOf(90.00323422222)
        );

        productService.saveNewProduct(input);

        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("should return ProductDto with field settled")
    void shouldReturnProductSavedFieldSettled() {
        var product = new Product();
        var uuid = UUID.randomUUID();
        var name = "Name";
        var description = "Description";
        var price = BigDecimal.TEN;

        product.setUuid(uuid);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        var input = new InputProduct(
            "Product Name",
            "Product Description",
            BigDecimal.valueOf(90.00323422222)
        );

        var dto = productService.saveNewProduct(input);

        verify(productRepository).save(any(Product.class));
        assertAll("Verify fields dto",
            () -> assertEquals(uuid, dto.getId()),
            () -> assertEquals(name, dto.getName()),
            () -> assertEquals(description, dto.getDescription()),
            () -> assertEquals(price, dto.getPrice())
        );
    }

    @Test
    @DisplayName("should return ProductDto with fields updated")
    void shouldReturnProductUpdate() {
        var product = new Product();
        var uuid = UUID.randomUUID();
        var name = "Name";
        var description = "Description";
        var price = BigDecimal.TEN;

        product.setUuid(uuid);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        when(productRepository.findById(uuid)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        var input = new InputProduct(
            "Product Name",
            "Product Description",
            BigDecimal.valueOf(90.00323422222)
        );

        var dto = productService.updateProduct(uuid, input);

        verify(productRepository).save(any(Product.class));
        verify(productRepository).findById(uuid);
        assertAll("Verify fields dto",
            () -> assertEquals(uuid, dto.getId()),
            () -> assertEquals(input.getName(), dto.getName()),
            () -> assertEquals(input.getDescription(), dto.getDescription()),
            () -> assertEquals(input.getPrice().setScale(3, RoundingMode.HALF_EVEN), dto.getPrice())
        );
    }

    @Test
    @DisplayName("should return list empty")
    void shouldReturnAllProductEmpty() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        var products = productService.findAll();

        verify(productRepository).findAll();

        assertNotNull(products);
        assertEquals(0, products.size());
    }

    @Test
    @DisplayName("should return list products")
    void shouldReturnAllProduct() {
        when(productRepository.findAll()).thenReturn(List.of(new Product()));

        var products = productService.findAll();

        verify(productRepository).findAll();

        assertNotNull(products);
        assertEquals(1, products.size());
    }

    @Test
    @DisplayName("should return list empty by search")
    void shouldReturnAllProductBySearch() {
        when(productRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        var products = productService.findAll("", BigDecimal.ONE, BigDecimal.TEN);

        verify(productRepository).findAll(any(Specification.class));

        assertNotNull(products);
        assertEquals(0, products.size());
    }

    @Test
    @DisplayName("should return product by id")
    void shouldReturnAProduct() {
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(new Product()));

        productService.findById(UUID.randomUUID());

        verify(productRepository).findById(any(UUID.class));
    }

    @Test
    @DisplayName("should throws exception")
    void shouldReturnAProductException() {
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.findById(UUID.randomUUID()));

        verify(productRepository).findById(any(UUID.class));
    }

}
