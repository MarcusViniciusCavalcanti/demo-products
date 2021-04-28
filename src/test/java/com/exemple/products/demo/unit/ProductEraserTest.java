package com.exemple.products.demo.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.exemple.products.demo.domain.product.entity.Product;
import com.exemple.products.demo.domain.product.repository.ProductRepository;
import com.exemple.products.demo.domain.product.usecase.ProductEraser;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Unit - Test ProductEraser")
@ExtendWith(MockitoExtension.class)
class ProductEraserTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductEraser productEraser;

    @Test
    void shouldDeleteProduct() {
        when(productRepository.findById(any())).thenReturn(Optional.of(new Product()));
        doNothing()
            .when(productRepository)
            .delete(any());

        productEraser.delete(UUID.randomUUID());

        var orderExecution = inOrder(productRepository);

        orderExecution.verify(productRepository).findById(any());
        orderExecution.verify(productRepository).delete(any(Product.class));
    }

    @Test
    void shouldThrowsExceptionWhenDeleteProduct() {
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productEraser.delete(UUID.randomUUID()));

        verify(productRepository).findById(any());
        verify(productRepository, never()).delete(any(Product.class));
    }
}
