package com.exemple.products.demo.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.exemple.products.demo.application.ProductService;
import com.exemple.products.demo.rest.endpoints.ProductsController;
import com.exemple.products.demo.rest.exception.IllegalRequestBodyException;
import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

@DisplayName("Unit - Test ProductsController")
@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @Mock
    private BindingResult bindingResult;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductsController productsController;

    @Test
    @DisplayName("should return Response with status code created and body is product data representation saved on dto")
    void shouldReturnResponseStatusCodeCreated() {
        var mock = mock(InputProduct.class);

        when(bindingResult.hasErrors()).thenReturn(Boolean.FALSE);
        when(productService.createNewProduct(mock)).thenReturn(ProductDto.builder().build());

        var response = productsController.create(mock);

        var orderExecution = inOrder(bindingResult, productService);

        orderExecution.verify(bindingResult).hasErrors();
        orderExecution.verify(productService).createNewProduct(mock);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
