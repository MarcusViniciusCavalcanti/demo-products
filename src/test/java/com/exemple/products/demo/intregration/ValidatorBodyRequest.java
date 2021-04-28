package com.exemple.products.demo.intregration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.exemple.products.demo.structure.dto.inputs.InputProduct;
import io.restassured.response.ValidatableResponse;
import java.util.UUID;

public final class ValidatorBodyRequest {

    static void shouldReturnErrorBadRequest(InputProduct input, int port) {
        validateResponse(given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .body(input)
            .when()
            .post("/products")
            .then(), "Request body is invalid");
    }

    static void shouldReturnExceptionWhenRequestBodyIsInvalid(String body, int port) {
        validateResponse(given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .body(body)
            .when()
            .post("/products")
            .then(), "Request body is invalid");
    }

    static void shouldReturnExceptionWhenNameExist(InputProduct input, int port) {
        validateResponse(given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .body(input)
            .when()
            .post("/products")
            .then(), "Name product is exist other Product");
    }

    static void shouldReturnExceptionWhenNameExist(InputProduct input, int port,  UUID uuid) {
        validateResponse(given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .body(input)
            .when()
            .pathParam("id", uuid.toString())
            .put("/products/{id}")
            .then(), "Name product is exist other Product");
    }

    static void shouldReturnExceptionWhenRequestBodyIsInvalid(String body, int port, UUID uuid) {
        validateResponse(given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .body(body)
            .when()
            .pathParam("id", uuid.toString())
            .put("/products/{id}")
            .then(), "Request body is invalid");
    }

    static void shouldReturnErrorBadRequest(InputProduct input, int port, UUID uuid) {
        validateResponse(given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .body(input)
            .when()
            .pathParam("id", uuid.toString())
            .put("/products/{id}")
            .then(), "Request body is invalid");
    }

    private static void validateResponse(ValidatableResponse then, String msg) {
        then.assertThat()
            .statusCode(BAD_REQUEST.value())
            .body("status_code", is(BAD_REQUEST.value()))
            .body("message", is(msg));
    }
}
