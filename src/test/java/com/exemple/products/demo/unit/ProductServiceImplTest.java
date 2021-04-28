package com.exemple.products.demo.unit;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.exemple.products.demo.application.impl.ProductServiceImpl;
import com.exemple.products.demo.domain.product.usecase.ProductCreator;
import com.exemple.products.demo.domain.product.usecase.ProductEraser;
import com.exemple.products.demo.domain.product.usecase.ProductGetter;
import com.exemple.products.demo.domain.product.usecase.ProductUpdater;
import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Unit - Test ProductServiceImpl")
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductCreator productCreator;

    @Mock
    private ProductUpdater productUpdater;

    @Mock
    private ProductGetter productGetter;

    @Mock
    private ProductEraser productEraser;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Should call domain in use case Product Creator")
    void shouldCallDomainLayerUseCaseCreated() {
        when(productCreator.create(any(InputProduct.class)))
            .thenReturn(ProductDto.builder().build());

        productService.createNewProduct(new InputProduct("Name", "Description", BigDecimal.TEN));

        verify(productCreator).create(any(InputProduct.class));
    }

    @Test
    @DisplayName("Should call domain in use case Product Update")
    void shouldCallDomainLayerUseCaseUpdated() {
        var input = new InputProduct("Name", "Description", BigDecimal.TEN);
        var uuid = UUID.randomUUID();
        when(productUpdater.update(eq(uuid), eq(input))).thenReturn(ProductDto.builder().build());

        productService.update(uuid, input);

        verify(productUpdater).update(eq(uuid), eq(input));
    }

    @Test
    @DisplayName("Should call domain in use case Product Find All")
    void shouldCallDomainLayerUseCaseGetterFindAll() {
        when(productGetter.findAll()).thenReturn(List.of(ProductDto.builder().build()));

        productService.findAll();

        verify(productGetter).findAll();
    }

    @Test
    @DisplayName("Should call domain in use case Product Find by filter")
    void shouldCallDomainLayerUseCaseGetterFindByFilter() {
        when(productGetter.findAllByFilter(any(), any(), any())).thenReturn(List.of(ProductDto.builder().build()));

        productService.findAllByFilter("name", BigDecimal.ZERO, BigDecimal.TEN);

        verify(productGetter).findAllByFilter(any(), any(), any());
    }

    @Test
    @DisplayName("Should call domain in use case Product find by id")
    void shouldCallDomainLayerUseCaseGetterFindById() {
        when(productGetter.findById(any())).thenReturn(ProductDto.builder().build());

        productService.findById(UUID.randomUUID());

        verify(productGetter).findById(any());
    }

    @Test
    @DisplayName("Should call domain in use case delete by id")
    void shouldDeleteProductById() {
        doNothing().when(productEraser).delete(any());

        productService.delete(UUID.randomUUID());

        verify(productEraser).delete(any());
    }
}
