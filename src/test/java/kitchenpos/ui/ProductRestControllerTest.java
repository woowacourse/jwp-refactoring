package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ui.dto.request.product.ProductRequest;
import kitchenpos.ui.dto.response.product.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

@DisplayName("ProductRestController 통합 테스트")
class ProductRestControllerTest extends IntegrationTest {

    @DisplayName("create 메서드는 Product 가격이 null이면 생성 예외가 발생한다.")
    @Test
    void create_product_price_null_exception_thrown() {
        // given
        ProductRequest request = new ProductRequest("순대튀김", null);

        // when, then
        webTestClient.post()
            .uri("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("유효하지 않은 Product 가격입니다.")
            );
    }

    @DisplayName("create 메서드는 Product 가격이 음수면 생성 예외가 발생한다.")
    @Test
    void create_product_price_negative_exception_thrown() {
        // given
        ProductRequest request = new ProductRequest("순대튀김", BigDecimal.valueOf(-1));

        // when, then
        webTestClient.post()
            .uri("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("유효하지 않은 Product 가격입니다.")
            );
    }

    @DisplayName("create 메서드는 Product 가격이 0 이상 양수면 정상적으로 생성한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 10})
    void it_saves_and_returns_product_with_url(int price) {
        // given
        ProductRequest request = new ProductRequest("순대튀김", BigDecimal.valueOf(price));
        ProductResponse expected = new ProductResponse(7L, "순대튀김", BigDecimal.valueOf(price));

        // when, then
        webTestClient.post()
            .uri("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectHeader()
            .valueEquals("location", "/api/products/7")
            .expectBody(ProductResponse.class)
            .value(response -> assertThat(response).usingRecursiveComparison()
                .isEqualTo(expected)
            );
    }

    @DisplayName("list 메서드는 모든 Product 목록을 조회한다.")
    @Test
    void list_returns_product_list() {
        // given, when, then
        webTestClient.get()
            .uri("/api/products")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(new ParameterizedTypeReference<List<ProductResponse>>() {
            })
            .value(response -> assertThat(response).extracting("name")
                .contains("후라이드", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨")
                .hasSize(6)
            );
    }
}
