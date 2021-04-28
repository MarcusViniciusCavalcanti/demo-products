package com.exemple.products.demo.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.exemple.products.demo.domain.product.repository.ProductRepository;
import com.exemple.products.demo.domain.product.service.SpecificationFilter;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@DisplayName("Unit - Test SpecificationFilter")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/insert-product.sql")
@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/delete-product.sql")
@DataJpaTest
class SpecificationFilterTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should return products with all filter")
    void shouldReturnProductsAll() {
        var filterName = SpecificationFilter.buildFilter("Name", BigDecimal.ZERO, BigDecimal.valueOf(Long.MAX_VALUE, 3));
        var products = productRepository.findAll(filterName);
        assertEquals(11, products.size());
    }

    @Test
    @DisplayName("Should return products with filter name")
    void shouldReturnProductsFilterName() {
        var filterName = SpecificationFilter.buildFilter("N", null, null);
        var products = productRepository.findAll(filterName);
        assertEquals(11, products.size());
    }

    @Test
    @DisplayName("Should return products with filter min")
    void shouldReturnProductsFilterMin() {
        var filterName = SpecificationFilter.buildFilter(null, BigDecimal.valueOf(200.0), null);
        var products = productRepository.findAll(filterName);
        assertEquals(3, products.size());
    }

    @Test
    @DisplayName("Should return products with filter max")
    void shouldReturnProductsFilterMax() {
        var filterName = SpecificationFilter.buildFilter(null, null, BigDecimal.valueOf(1012.0));
        var products = productRepository.findAll(filterName);
        assertEquals(10, products.size());
    }
}
