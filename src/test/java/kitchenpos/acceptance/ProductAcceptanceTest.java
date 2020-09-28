package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("상품(Product) 관리")
class ProductAcceptanceTest {

    @LocalServerPort
    protected int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    /**
     * Feature: 상품을 등록한다. Scenario: 상품을 등록한다.
     *
     * When 상품을 등록한다. Then 상품이 등록되었다.
     * When 이미 존재하는 상품과 같은 이름의 상품을 등록한다. Then 상품이 등록되었다.
     */
    @Test
    @DisplayName("상품 등록")
    void createProduct() {
        Map<String, String> body = new HashMap<>();

        String productName = "후라이드 치킨";
        int productPrice = 9_000;

        body.put("name", productName);
        body.put("price", Integer.toString(productPrice));

        Product responseBody = sendCreateProductRequest(body);

        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getName()).isEqualTo(productName);
        assertThat(responseBody.getPrice().intValue()).isEqualTo(productPrice);
    }

    private Product sendCreateProductRequest(Map<String, String> body) {
        return
            given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post("/api/products")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(Product.class);
    }
}
