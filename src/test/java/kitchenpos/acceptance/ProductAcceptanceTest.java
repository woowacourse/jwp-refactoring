package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.ui.dto.product.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ProductAcceptanceTest extends AcceptanceTest {

    private static final String API = "/api/products";

    @DisplayName("새로운 상품을 생성할 수 있다.")
    @Test
    void createProduct() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("name", 1000L);

        ExtractableResponse<Response> response = HttpMethodFixture.httpPost(productCreateRequest, API);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("모든 상품을 조회할 수 있다.")
    @Test
    void listProduct() {
        ExtractableResponse<Response> response = HttpMethodFixture.httpGet(API);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
