package com.exemple.products.demo.unit;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.exemple.products.demo.application.impl.ProductServiceImpl;
import com.exemple.products.demo.domain.product.usecase.ProductCreator;
import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Unit - Test ProductServiceImpl")
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductCreator productCreator;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Should call domain in use case Product Creator")
    void shouldCallDomainLayerUseCase() {
        when(productCreator.create(any(InputProduct.class)))
            .thenReturn(ProductDto.builder().build());

        productService.createNewProduct(new InputProduct("Name", "Description", BigDecimal.TEN));

        verify(productCreator).create(any(InputProduct.class));
    }
}
