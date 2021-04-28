package com.exemple.products.demo.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.exemple.products.demo.domain.product.service.ProductService;
import com.exemple.products.demo.domain.product.usecase.ProductGetter;
import com.exemple.products.demo.structure.dto.ProductDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@DisplayName("Unit - Test ProductGetter")
@ExtendWith(MockitoExtension.class)
class ProductGetterTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductGetter productGetter;

    @DisplayName("Should return all products")
    @Test
    void shouldReturnAllProducts() {
        when(productService.findAll()).thenReturn(List.of(ProductDto.builder().build()));

        productGetter.findAll();

        verify(productService).findAll();
    }

    @DisplayName("Should return all products only filter name")
    @Test
    void shouldReturnAllProductsFilterName() {
        var name = "Name";
        when(productService.findAll(name, BigDecimal.ZERO, BigDecimal.valueOf(Long.MAX_VALUE, 3)))
            .thenReturn(List.of(ProductDto.builder().build()));

        productGetter.findAllByFilter(name, null, null);

        verify(productService).findAll(
            argThat(nameArgs -> nameArgs.equals(name)),
            argThat(min -> BigDecimal.ZERO.compareTo(min) == 0),
            argThat(max -> BigDecimal.valueOf(Long.MAX_VALUE, 3).compareTo(max) == 0)
        );
    }

    @DisplayName("Should return all products only filter min")
    @Test
    void shouldReturnAllProductsFilterMin() {
        when(productService.findAll("", BigDecimal.TEN, BigDecimal.valueOf(Long.MAX_VALUE, 3)))
            .thenReturn(List.of(ProductDto.builder().build()));

        productGetter.findAllByFilter(null, BigDecimal.TEN, null);

        verify(productService).findAll(
            argThat(String::isBlank),
            argThat(min -> BigDecimal.TEN.compareTo(min) == 0),
            argThat(max -> BigDecimal.valueOf(Long.MAX_VALUE, 3).compareTo(max) == 0)
        );
    }

    @DisplayName("Should return all products only filter max")
    @Test
    void shouldReturnAllProductsFilterMax() {
        when(productService.findAll("", BigDecimal.ZERO, BigDecimal.valueOf(105.0)))
            .thenReturn(List.of(ProductDto.builder().build()));

        productGetter.findAllByFilter(null, null, BigDecimal.valueOf(105.0));

        verify(productService).findAll(
            argThat(String::isBlank),
            argThat(min -> BigDecimal.ZERO.compareTo(min) == 0),
            argThat(max -> BigDecimal.valueOf(105.0).compareTo(max) == 0)
        );
    }

    @Test
    @DisplayName("Should return products by id")
    void should() {
        when(productService.findById(any())).thenReturn(ProductDto.builder().build());

        productGetter.findById(UUID.randomUUID());

        verify(productService).findById(any());
    }
}
