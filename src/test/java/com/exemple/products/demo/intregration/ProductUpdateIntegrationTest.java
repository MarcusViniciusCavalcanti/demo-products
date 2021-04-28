package com.exemple.products.demo.intregration;

import static io.restassured.RestAssured.given;
import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.exemple.products.demo.domain.product.repository.ProductRepository;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Update Product Integration Test")
public class ProductUpdateIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("should return bad request in update when the Name field is invalid")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void shouldReturnErrorBadRequestWhenNameIsInvalid(String name) {
        var input = new InputProduct(name, "Description", BigDecimal.TEN);
        ValidatorBodyRequest.shouldReturnErrorBadRequest(input, port, UUID.randomUUID());
    }

    @DisplayName("should return bad request in update when field Description is invalid")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void shouldReturnErrorBadRequestWhenDescriptionIsInvalid(String description) {
        var input = new InputProduct("Product Name", description, BigDecimal.TEN);
        ValidatorBodyRequest.shouldReturnErrorBadRequest(input, port, UUID.randomUUID());
    }

    @DisplayName("should return bad request in update when field price is less then zero or equals zero")
    @ParameterizedTest
    @ValueSource(doubles = {0.0, -10.0, -0.1})
    void shouldReturnErrorBadRequestWhenPriceIsLessThanZero(double price) {
        var input =
            new InputProduct("Product Name", "Product Description", BigDecimal.valueOf(price));
        ValidatorBodyRequest.shouldReturnErrorBadRequest(input, port, UUID.randomUUID());
    }

    @DisplayName("should return bad request in update request body is invalid miss field or miss brackets, are example")
    @ParameterizedTest
    @ValueSource(strings = {
        "{ \"name\" : \"name\"}",
        "{ \"description\" : \"description\"}",
        "{ \"price\" : \"1.00\"}",
        "{ \"name\" : \"1.00\"}",
        "{ \"price\" : \"Name\"}",
        "{ \"otherField\" : \"Name\"}",
    })
    void shouldReturnExceptionWhenRequestBodyIsInvalid(String body) {
        ValidatorBodyRequest.shouldReturnExceptionWhenRequestBodyIsInvalid(body, port, UUID.randomUUID());
    }

    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/insert-product.sql")
    @Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/delete-product.sql")
    @DisplayName("should return bad request in update when name exist in other product")
    @Test
    void shouldExceptionWhenNameExist() {
        var input =
            new InputProduct("Product Name 1", "Product Description", BigDecimal.TEN);
        ValidatorBodyRequest.shouldReturnExceptionWhenNameExist(input, port, UUID.randomUUID());
    }

    @DisplayName("Should return not found when update product with id not found in database")
    @Test
    void shouldReturnExceptionWhenNotFound() {
        var input =
            new InputProduct("Product Name 1", "Product Description", BigDecimal.TEN);
        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .body(input)
            .when()
            .pathParam("id", UUID.randomUUID().toString())
            .put("/products/{id}")
            .then()
            .assertThat()
            .statusCode(NOT_FOUND.value());
    }

    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/insert-product.sql")
    @Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/delete-product.sql")
    @DisplayName("Should return Product updated")
    @Test
    void shouldHaveUpdateProduct() {
        var product = productRepository.findAll().get(0);

        var input =
            new InputProduct("Product Name", "Product Description", BigDecimal.valueOf(100.00));
        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .body(input)
            .when()
            .pathParam("id", product.getUuid().toString())
            .put("/products/{id}")
            .then()
            .assertThat()
            .statusCode(OK.value())
            .body("id", is(product.getUuid().toString()))
            .body("name", is(input.getName()))
            .body("description", is(input.getDescription()))
            .body("price", is(100.00f));

        productRepository.findById(product.getUuid()).ifPresent(prod -> {
            assertEquals(input.getName(), prod.getName());
            assertEquals(input.getDescription(), prod.getDescription());
            assertEquals(input.getPrice().compareTo(prod.getPrice()), 0);
        });
    }
}
