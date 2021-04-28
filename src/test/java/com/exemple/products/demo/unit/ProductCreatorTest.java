package com.exemple.products.demo.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.exemple.products.demo.domain.product.exception.ProductNameException;
import com.exemple.products.demo.domain.product.repository.ProductRepository;
import com.exemple.products.demo.domain.product.service.ProductService;
import com.exemple.products.demo.domain.product.service.ProductValidator;
import com.exemple.products.demo.domain.product.usecase.ProductCreator;
import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Unit - Test ProductCreator")
@ExtendWith(MockitoExtension.class)
class ProductCreatorTest {

    @Mock
    private ProductValidator productValidator;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductCreator productCreator;

    @DisplayName("Should throws ProductNameException when name is exist in other product")
    @Test
    void shouldThrowsException() {
        var input =
            new InputProduct("Product Name", "Product Description", BigDecimal.TEN);

        doThrow(new ProductNameException(input.getName()))
            .when(productValidator)
            .checkNameExist(input.getName());

        assertThrows(ProductNameException.class, () -> productCreator.create(input));
    }

    @Test
    @DisplayName("should return product data representation saved on dto")
    void shouldReturnDto() {
        var input =
            new InputProduct("Product Name", "Product Description", BigDecimal.TEN);

        doNothing().when(productValidator).checkNameExist(input.getName());
        when(productService.saveNewProduct(eq(input))).thenReturn(ProductDto.builder().build());

        productCreator.create(input);

        var orderExecution = Mockito.inOrder(productValidator, productService);

        orderExecution.verify(productValidator).checkNameExist(input.getName());
        orderExecution.verify(productService).saveNewProduct(eq(input));
    }
}
