package com.exemple.products.demo.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.exemple.products.demo.domain.product.exception.ProductNameException;
import com.exemple.products.demo.domain.product.repository.ProductRepository;
import com.exemple.products.demo.domain.product.service.ProductValidator;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@DisplayName("Unit - Test ProductValidator")
@ExtendWith(MockitoExtension.class)
class ProductValidatorTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductValidator productValidator;

    @DisplayName("Should throws ProductNameException when name is exist in other product")
    @Test
    void shouldThrowsExceptionExistName() {
        when(productRepository.existsProductByName("Name")).thenReturn(Boolean.TRUE);

        var exception =
            assertThrows(ProductNameException.class, () -> productValidator.checkNameExist("Name"));
        assertEquals("this name [Name] is exist in other product", exception.getMessage());
    }

    @DisplayName("Should throws ProductNameException when name is exist in other product")
    @Test
    void shouldThrowsExceptionExistNameWithId() {
        var uuid = UUID.randomUUID();
        when(productRepository.existsProductByNameAndUuidNot("Name", uuid))
            .thenReturn(Boolean.TRUE);

        var exception =
            assertThrows(ProductNameException.class,
                () -> productValidator.checkNameExist(uuid, "Name"));
        assertEquals("this name [Name] is exist in other product", exception.getMessage());
    }
}
