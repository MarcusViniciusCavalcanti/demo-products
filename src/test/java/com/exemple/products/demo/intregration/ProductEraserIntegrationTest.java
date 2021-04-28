package com.exemple.products.demo.intregration;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.exemple.products.demo.domain.product.repository.ProductRepository;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Eraser Product Integration Test")
public class ProductEraserIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("should delete product by id")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/insert-product.sql")
    @Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/delete-product.sql")
    void shouldDeleteProduct() {
        var product = productRepository.findAll().get(0);

        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .pathParam("id", product.getUuid())
            .delete("/products/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value());

        assertFalse(productRepository.findById(product.getUuid()).isPresent());
    }

    @Test
    @DisplayName("should throws not found when id not exist in database")
    void shouldThrowsExceptionWhenNotFound() {
        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .pathParam("id", UUID.randomUUID())
            .delete("/products/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
