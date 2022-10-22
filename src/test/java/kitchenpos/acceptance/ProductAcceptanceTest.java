package kitchenpos.acceptance;


import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import java.util.HashMap;
import java.util.Map;
import kitchenpos.acceptance.common.Request;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("Production 은 ")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 추가해야 한다.")
    @Test
    void createProduct() {
        Map<String, Object> requestBody = Map.of(
                "name", "productName",
                "price", 1000);
        ExtractableResponse<Response> response = Request.create("/api/products", requestBody);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("상품 목록을 가져와야 한다.")
    @Test
    void getProducts() {
        Map<String, Object> requestBody = Map.of(
                "name", "productName",
                "price", 1000);
        Request.create("/api/products", requestBody);

        ResponseBodyExtractionOptions body = Request.get("/api/products")
                .body();
        java.util.List<Product> list = body.jsonPath().getList(".", Product.class);

        assertThat(list.size()).isEqualTo(1);
    }
}
