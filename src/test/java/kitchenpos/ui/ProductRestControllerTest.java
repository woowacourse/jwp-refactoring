package kitchenpos.ui;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;

class ProductRestControllerTest extends AcceptanceTest {
    @Test
    @DisplayName("저장된 상품들을 조회한다.")
    void list() {
        // when, then
        get("/api/products")
                .then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("name", hasItems("후라이드", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨"));
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        // given
        ProductRequest productRequest = new ProductRequest("product", 10000L);

        // when
        ExtractableResponse<Response> response = post("/api/products", productRequest)
                .then().extract();
        ProductResponse productResponse = response.as(ProductResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(productResponse.getName()).isEqualTo(productRequest.getName());
        assertThat(productResponse.getPrice()).isEqualTo(productRequest.getPrice());
    }
}
