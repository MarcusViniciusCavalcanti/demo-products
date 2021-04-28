package com.exemple.products.demo.intregration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Create Product Integration Test")
public class ProductCreateIntegrationTest extends IntegrationAbstractTest {

    @DisplayName("should return bad request when the Name field is invalid")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void shouldReturnErrorBadRequestWhenNameIsInvalid(String name) {
        var input = new InputProduct(name, "Description", BigDecimal.TEN);

        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .body(input)
            .when()
            .post("/products")
            .then()
            .assertThat()
            .statusCode(BAD_REQUEST.value())
            .body("status_code", is(BAD_REQUEST.value()))
            .body("message", is("Request body is invalid"));
    }

    @DisplayName("should return Bad Requet when field Description is invalid")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void shouldReturnErrorBadRequestWhenDescriptionIsInvalid(String description) {
        var input = new InputProduct("Product Name", description, BigDecimal.TEN);

        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .body(input)
            .when()
            .post("/products")
            .then()
            .assertThat()
            .statusCode(BAD_REQUEST.value())
            .body("status_code", is(BAD_REQUEST.value()))
            .body("message", is("Request body is invalid"));
    }

    @DisplayName("should return bad request when field price is less then zero or equals zero")
    @ParameterizedTest
    @ValueSource(doubles = {0.0, -10.0, -0.1})
    void shouldReturnErrorBadRequestWhenPriceIsLessThanZero(double price) {
        var input =
            new InputProduct("Product Name", "Product Description", BigDecimal.valueOf(price));

        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .body(input)
            .when()
            .post("/products")
            .then()
            .assertThat()
            .statusCode(BAD_REQUEST.value())
            .body("status_code", is(BAD_REQUEST.value()))
            .body("message", is("Request body is invalid"));
    }

    @DisplayName("should return bad request when field price is null")
    @Test
    void shouldReturnErrorBadRequestWhenPriceIsNull() {
        var input = new InputProduct("Product Name", "Product Description", null);

        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .body(input)
            .when()
            .post("/products")
            .then()
            .assertThat()
            .statusCode(BAD_REQUEST.value())
            .body("status_code", is(BAD_REQUEST.value()))
            .body("message", is("Request body is invalid"));
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
        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .body(body)
            .when()
            .post("/products")
            .then()
            .assertThat()
            .statusCode(BAD_REQUEST.value())
            .body("status_code", is(BAD_REQUEST.value()))
            .body("message", is("Request body is invalid"));
    }
}
