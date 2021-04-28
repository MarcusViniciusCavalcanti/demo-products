package com.exemple.products.demo.intregration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.exemple.products.demo.domain.product.repository.ProductRepository;
import com.exemple.products.demo.structure.dto.ProductDto;
import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import java.math.BigDecimal;
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
@DisplayName("Create Product Integration Test")
public class ProductCreateIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("should return bad request when the Name field is invalid")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void shouldReturnErrorBadRequestWhenNameIsInvalid(String name) {
        var input = new InputProduct(name, "Description", BigDecimal.TEN);
        ValidatorBodyRequest.shouldReturnErrorBadRequest(input, port);
    }

    @DisplayName("should return bad request when field Description is invalid")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void shouldReturnErrorBadRequestWhenDescriptionIsInvalid(String description) {
        var input = new InputProduct("Product Name", description, BigDecimal.TEN);
        ValidatorBodyRequest.shouldReturnErrorBadRequest(input, port);
    }

    @DisplayName("should return bad request when field price is less then zero or equals zero")
    @ParameterizedTest
    @ValueSource(doubles = {0.0, -10.0, -0.1})
    void shouldReturnErrorBadRequestWhenPriceIsLessThanZero(double price) {
        var input =
            new InputProduct("Product Name", "Product Description", BigDecimal.valueOf(price));
        ValidatorBodyRequest.shouldReturnErrorBadRequest(input, port);
    }

    @DisplayName("should return bad request when field price is null")
    @Test
    void shouldReturnErrorBadRequestWhenPriceIsNull() {
        var input = new InputProduct("Product Name", "Product Description", null);
        ValidatorBodyRequest.shouldReturnErrorBadRequest(input, port);
    }

    @DisplayName("should return bad request when request body is invalid miss field or miss brackets, are example")
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
        ValidatorBodyRequest.shouldReturnExceptionWhenRequestBodyIsInvalid(body, port);
    }

    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/insert-product.sql")
    @Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/delete-product.sql")
    @DisplayName("should return bad request when name exist in other product")
    @Test
    void shouldExceptionWhenNameExist() {
        var input =
            new InputProduct("Product Name 1", "Product Description", BigDecimal.TEN);
        ValidatorBodyRequest.shouldReturnExceptionWhenNameExist(input, port);
    }

    @DisplayName("should return product saved")
    @Test
    void shouldReturnProduct() {
        var input =
            new InputProduct("Product Name", "Product Description", BigDecimal.TEN);

        var response = given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .body(input)
            .when()
            .post("/products")
            .then()
            .assertThat()
            .statusCode(CREATED.value())
            .body("id", notNullValue())
            .body("name", is(input.getName()))
            .body("description", is(input.getDescription()))
            .body("price", is(10.0f))
            .extract().body().as(ProductDto.class);

        productRepository.findById(response.getId()).ifPresent(product -> {
            assertEquals(product.getUuid(), response.getId());
            assertEquals(product.getName(), response.getName());
            assertEquals(product.getDescription(), response.getDescription());
            assertEquals(product.getPrice(), response.getPrice().setScale(2));
        });

        productRepository.deleteAll();
    }
}
