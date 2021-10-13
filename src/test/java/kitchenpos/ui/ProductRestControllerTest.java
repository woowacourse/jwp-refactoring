package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

@DisplayName("ProductRestController 통합 테스트")
class ProductRestControllerTest extends IntegrationTest {

    @DisplayName("create 메서드는")
    @Nested
    class Describe_create {

        @DisplayName("생성하려는 Product의 Price가 null이면")
        @Nested
        class Context_price_null {

            @DisplayName("Product를 생성하지 못하고 예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Product product = new Product();
                product.setName("순대튀김");
                product.setPrice(null);

                // when, then
                webTestClient.post()
                    .uri("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(product)
                    .exchange()
                    .expectStatus()
                    .is5xxServerError();
            }
        }

        @DisplayName("생성하려는 Product의 Price가 음수면")
        @Nested
        class Context_price_negative {

            @DisplayName("Product를 생성하지 못하고 예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Product product = new Product();
                product.setName("순대튀김");
                product.setPrice(BigDecimal.valueOf(-1));

                // when, then
                webTestClient.post()
                    .uri("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(product)
                    .exchange()
                    .expectStatus()
                    .is5xxServerError();
            }
        }

        @DisplayName("생성하려는 Product의 Price가 양수면")
        @Nested
        class Context_price_positive {

            @DisplayName("요청한 Product를 생성하고 상응하는 Product 및 URL을 받는다.")
            @Test
            void it_saves_and_returns_product_with_url() {
                // given
                Product product = new Product();
                product.setName("순대튀김");
                product.setPrice(BigDecimal.valueOf(100));
                Product expected = new Product();
                expected.setId(7L);
                expected.setName("순대튀김");
                expected.setPrice(BigDecimal.valueOf(100));

                // when, then
                webTestClient.post()
                    .uri("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(product)
                    .exchange()
                    .expectStatus()
                    .isCreated()
                    .expectHeader()
                    .value("location", startsWith("/api/products/7"))
                    .expectBody(MenuGroup.class)
                    .value(response -> assertThat(response).usingRecursiveComparison()
                        .isEqualTo(expected)
                    );
            }
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class Describe_list {

        @DisplayName("모든 Product 목록을 조회한다.")
        @Test
        void it_returns_product_list() {
            // given, when, then
            webTestClient.get()
                .uri("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<Product>>(){})
                .value(response -> assertThat(response).extracting("name")
                    .contains("후라이드", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨")
                    .hasSize(6)
                );
        }
    }
}
