package com.exemple.products.demo.intregration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.exemple.products.demo.domain.product.repository.ProductRepository;
import org.hamcrest.Matchers;
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
@DisplayName("Getter Product Integration Test")
public class ProductGetterIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/insert-product.sql")
    @Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/delete-product.sql")
    @DisplayName("Should return all products in database (see insert-product.sql in resource)")
    void shouldReturnAllProducts() {
        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .get("/products")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(11));
    }

    @Test
    @DisplayName("Should return array products zero elements")
    void shouldReturnAllProductsZeroArray() {
        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .get("/products")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(0));
    }

    @Test
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/insert-product.sql")
    @Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/delete-product.sql")
    @DisplayName("Should return products by filter name in database (see insert-product.sql in resource)")
    void shouldReturnAllProductsByFilterName() {
        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .queryParam("q", "Product")
            .get("/products/search")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(11));
    }

    @Test
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/insert-product.sql")
    @Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/delete-product.sql")
    @DisplayName("Should return products by filter min price in database (see insert-product.sql in resource)")
    void shouldReturnAllProductsByFilterMin() {
        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .queryParam("min_price", 1011.0)
            .get("/products/search")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(1));
    }

    @Test
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/insert-product.sql")
    @Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/delete-product.sql")
    @DisplayName("Should return products by filter max price in database (see insert-product.sql in resource)")
    void shouldReturnAllProductsByFilterMax() {
        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .queryParam("max_price", 200.0)
            .get("/products/search")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(8));
    }

    @Test
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/insert-product.sql")
    @Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/delete-product.sql")
    @DisplayName("Should return products by filter but not value in search  in database (see insert-product.sql in resource)")
    void shouldReturnAllProductsByFilterNotValue() {
        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .get("/products/search")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(11));
    }

    @Test
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/insert-product.sql")
    @Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/delete-product.sql")
    @DisplayName("Should return products by id in database (see insert-product.sql in resource)")
    void shouldReturnAProductsByFId() {
        var product = productRepository.findAll().get(0);

        given()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .port(port)
            .pathParam("id", product.getUuid())
            .get("/products/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("id", is(product.getUuid().toString()))
            .body("name", is(product.getName()))
            .body("description", is(product.getDescription()))
            .body("price", is(product.getPrice().floatValue()));
    }
}
