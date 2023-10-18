package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import kitchenpos.domain.Product.Product;
import kitchenpos.ui.request.ProductCreateRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
class ProductIntegrationTest extends IntegrationTest {

    @Nested
    class 상품_등록시 {

        @ParameterizedTest
        @NullAndEmptySource
        void 상품명이_비어있다면_에러가_발생한다(String productName) {
            //given
            final var request = new ProductCreateRequest(productName, new BigDecimal("1000"));

            //when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/products", request, Product.class);

            //then
            assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        void 상품명이_255자_이상이라면_에러가_발생한다() {
            //given
            final var invalidName = "상품명".repeat(256);
            final var price = new BigDecimal("1000");
            final var request = new ProductCreateRequest(invalidName, price);

            //when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/products", request, Product.class);

            //then
            assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"-1", "12345123451234512345"})
        void 가격이_null_이거나_올바른_범위가_아니라면_에러가_발생한다(BigDecimal invalidPrice) {
            //given
            final var request = new ProductCreateRequest("상품명", invalidPrice);

            //when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/products", request, Product.class);

            //then
            assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        void 정상적으로_등록한다() {
            //given
            final var request = new ProductCreateRequest("상품명", new BigDecimal("1000"));

            //when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/products", request, Product.class);

            //then
            final var body = response.getBody();
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                    () -> assertThat(body.getId()).isNotNull(),
                    () -> assertThat(body.getName()).isEqualTo(request.getName()),
                    () -> assertEquals(body.getPrice().setScale(2, RoundingMode.HALF_UP), request.getPrice().setScale(2, RoundingMode.HALF_UP))
            );
        }
    }

    @Nested
    class 상품_목록_조회 {

        @Test
        void 상품을_정상적으로_조회한다() {
            //given
            final var request = new ProductCreateRequest("상품명", new BigDecimal("1000"));
            restTemplate.postForEntity("http://localhost:" + port + "/api/products", request, ProductCreateRequest.class);

            //when
            final var response = restTemplate.getForEntity("http://localhost:" + port + "/api/products", Product[].class);

            //then
            final var body = response.getBody();
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(body).hasSize(1),
                    () -> assertThat(body[0].getId()).isNotNull(),
                    () -> assertThat(body[0].getName()).isEqualTo(request.getName()),
                    () -> assertEquals(body[0].getPrice().setScale(2, RoundingMode.HALF_UP), request.getPrice().setScale(2, RoundingMode.HALF_UP))
            );
        }
    }
}
