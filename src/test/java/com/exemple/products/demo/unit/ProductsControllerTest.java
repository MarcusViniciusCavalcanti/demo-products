package com.exemple.products.demo.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.exemple.products.demo.application.ProductService;
import com.exemple.products.demo.rest.endpoints.ProductsController;
import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import com.exemple.products.demo.structure.dto.inputs.ParamsSearch;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@DisplayName("Unit - Test ProductsController")
@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductsController productsController;

    @Test
    @DisplayName("should return Response with status code created and body is product data representation saved on dto")
    void shouldReturnResponseStatusCodeCreated() {
        var mock = mock(InputProduct.class);

        when(productService.createNewProduct(mock)).thenReturn(ProductDto.builder().build());

        var response = productsController.create(mock);

        verify(productService).createNewProduct(mock);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("should return Response with status code OK and body is product data representation updated on dto")
    void shouldReturnResponseStatusCodeOk() {
        var mock = mock(InputProduct.class);
        var uuid = UUID.randomUUID();

        when(productService.update(uuid, mock)).thenReturn(ProductDto.builder().build());

        var response = productsController.update(mock, uuid);

        verify(productService).update(uuid, mock);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("should return Response with status code ok and body list products")
    void shouldReturnResponseOkWhenCallFindAll() {
        when(productService.findAll()).thenReturn(Collections.emptyList());
        
        var response  = productsController.findAll();

        verify(productService).findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @ParameterizedTest
    @MethodSource("providerArgumentsFilter")
    @DisplayName("should return Response with status code ok and body list products with list filter name")
    void shouldReturnResponseOkWhenCallFindAllByFilter(String q, Double min, Double max, BigDecimal minBig, BigDecimal maxBig) {
        when(productService.findAllByFilter(q, minBig, maxBig)).thenReturn(Collections.emptyList());

        var params = new ParamsSearch();
        params.setQ(q);
        params.setMin_price(min);
        params.setMax_price(max);

        var response  = productsController.findAllByFilter(params);

        verify(productService).findAllByFilter(q, minBig, maxBig);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("should return Response with status code ok and body product")
    void shouldFindById() {
        when(productService.findById(any())).thenReturn(ProductDto.builder().build());

        var response = productsController.findById(UUID.randomUUID());

        verify(productService).findById(any());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("should return ok but no body when delete product")
    void shouldReturnOkWhenDeleteProduct() {
        doNothing()
            .when(productService)
            .delete(any());

        var response = productsController.delete(UUID.randomUUID());

        verify(productService).delete(any());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    private static Stream<Arguments> providerArgumentsFilter() {
        return Stream.of(
          Arguments.of("name", null, null, null, null),
          Arguments.of(null, 10.0, null, BigDecimal.valueOf(10.0), null),
          Arguments.of(null, null, 10.0, null, BigDecimal.valueOf(10.0))
        );
    }
}
