package com.exemple.products.demo.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import com.exemple.products.demo.domain.product.exception.ProductNameException;
import com.exemple.products.demo.domain.product.service.ProductService;
import com.exemple.products.demo.domain.product.service.ProductValidator;
import com.exemple.products.demo.domain.product.usecase.ProductUpdater;
import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Unit - Test ProductUpdater")
@ExtendWith(MockitoExtension.class)
class ProductUpdaterTest {

    @Mock
    private ProductValidator productValidator;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductUpdater productUpdater;

    @DisplayName("Should throws ProductNameException when name is exist in other product")
    @Test
    void shouldThrowsException() {
        var input =
            new InputProduct("Product Name", "Product Description", BigDecimal.TEN);

        var uuid = UUID.randomUUID();
        doThrow(new ProductNameException(input.getName()))
            .when(productValidator)
            .checkNameExist(uuid, input.getName());

        assertThrows(ProductNameException.class, () -> productUpdater.update(uuid, input));
    }

    @Test
    @DisplayName("should return product data representation updated on dto")
    void shouldReturnDto() {
        var uuid = UUID.randomUUID();
        var input =
            new InputProduct("Product Name", "Product Description", BigDecimal.TEN);

        doNothing().when(productValidator).checkNameExist(eq(uuid), eq(input.getName()));
        when(productService.updateProduct(eq(uuid), eq(input)))
            .thenReturn(ProductDto.builder().build());

        productUpdater.update(uuid, input);

        var orderExecution = inOrder(productValidator, productService);

        orderExecution.verify(productValidator).checkNameExist(eq(uuid), eq(input.getName()));
        orderExecution.verify(productService).updateProduct(eq(uuid), eq(input));
    }
}
