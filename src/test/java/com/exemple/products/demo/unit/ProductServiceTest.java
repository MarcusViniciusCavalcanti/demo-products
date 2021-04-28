package com.exemple.products.demo.unit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.exemple.products.demo.domain.product.entity.Product;
import com.exemple.products.demo.domain.product.repository.ProductRepository;
import com.exemple.products.demo.domain.product.service.ProductService;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


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
}
