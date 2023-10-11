package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductRestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    class 상품_등록 {

        @Test
        void 상품을_정상적으로_등록한다() {
            //given
            final var product = new Product();
            product.setName("상품명");
            product.setPrice(new BigDecimal("1000"));

            //when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/products", product, Product.class);

            //then
            final var body = response.getBody();
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                    () -> assertThat(body.getId()).isNotNull(),
                    () -> assertThat(body.getName()).isEqualTo(product.getName()),
                    () -> assertEquals(body.getPrice().setScale(2, RoundingMode.HALF_UP), product.getPrice().setScale(2, RoundingMode.HALF_UP))
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 상품을_등록할_때_상품명이_비어있다면_에러가_발생한다(String productName) {
            //given
            final var product = new Product();
            product.setName(productName);
            product.setPrice(new BigDecimal("1000"));

            //when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/products", product, Product.class);

            //then
            assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        void 상품을_등록할_때_상품명이_255자_이상이라면_에러가_발생한다() {
            //given
            final var product = new Product();
            product.setName("상품명".repeat(256));
            product.setPrice(new BigDecimal("1000"));

            //when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/products", product, Product.class);

            //then
            assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "0", "12345123451234512345"})
        void 상품을_등록할_때_가격이_올바른_범위가_아니라면_에러가_발생한다(String price) {
            //given
            final var product = new Product();
            product.setName("상품명");
            product.setPrice(new BigDecimal(price));

            //when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/products", product, Product.class);

            //then
            assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Nested
    class 상품_목록_조회 {

        @Test
        void 상품을_정상적으로_조회한다() {
            //given
            final var product = new Product();
            product.setName("상품명");
            product.setPrice(new BigDecimal("1000"));
            restTemplate.postForEntity("http://localhost:" + port + "/api/products", product, Product.class);

            //when
            final var response = restTemplate.getForEntity("http://localhost:" + port + "/api/products", Product[].class);

            //then
            final var body = response.getBody();
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(body).hasSize(1),
                    () -> assertThat(body[0].getId()).isNotNull(),
                    () -> assertThat(body[0].getName()).isEqualTo(product.getName()),
                    () -> assertEquals(body[0].getPrice().setScale(2, RoundingMode.HALF_UP), product.getPrice().setScale(2, RoundingMode.HALF_UP))
            );
        }
    }
}
