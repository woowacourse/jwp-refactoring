package kitchenpos.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRestControllerTest extends ControllerTest {
    @Test
    @DisplayName("Product 생성")
    void create() {
        Product product = new Product();
        product.setName("떡볶이");
        product.setPrice(BigDecimal.valueOf(10000));

        ExtractableResponse<Response> response = postProduct(product);
        Product savedProduct = response.as(Product.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(savedProduct.getId()).isNotNull();
    }

    @Test
    @DisplayName("모든 Product 조회")
    void list() {
        Product product = new Product();
        product.setName("떡볶이");
        product.setPrice(BigDecimal.valueOf(10000));
        postProduct(product);

        Product product1 = new Product();
        product1.setName("순대");
        product1.setPrice(BigDecimal.valueOf(3000));
        postProduct(product1);

        ExtractableResponse<Response> response = getProducts();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().as(List.class)).hasSize(2);
    }

    static ExtractableResponse<Response> postProduct(Product product) {
        return RestAssured
                .given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when().post("/api/products")
                .then().log().all().extract();
    }

    static ExtractableResponse<Response> getProducts() {
        return RestAssured
                .given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all().extract();
    }
}
