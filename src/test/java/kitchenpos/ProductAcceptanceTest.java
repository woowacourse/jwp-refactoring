package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.ProductFixture.스키야키;
import static kitchenpos.fixture.ProductFixture.우동;
import static kitchenpos.step.ProductStep.상품_생성_요청;
import static kitchenpos.step.ProductStep.상품_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

class ProductAcceptanceTest extends AcceptanceTest {

    @Nested
    class ProductTest {

        @Test
        void 상품을_생성한다() {
            final Product product = 스키야키();
            final ExtractableResponse<Response> response = 상품_생성_요청(product);

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                    () -> assertThat(response.jsonPath().getObject("", Product.class))
                            .usingRecursiveComparison()
                            .ignoringFields("id", "price")
                            .isEqualTo(product),
                    () -> assertThat((int) response.jsonPath().getDouble("price"))
                            .isEqualTo(product.getPrice().intValue())
            );
        }

        @Test
        void 상품의_가격은_0원_이상이어야_한다() {
            final Product product = 스키야키();
            product.setPrice(BigDecimal.valueOf(-1));

            final ExtractableResponse<Response> response = 상품_생성_요청(product);

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }
    }

    @Nested
    class ProductQueryTest {

        @Test
        void 상품을_조회한다() {
            final List<Product> products = List.of(스키야키(), 우동());
            for (final Product product : products) {
                상품_생성_요청(product);
            }

            final ExtractableResponse<Response> response = 상품_조회_요청();
            final List<Product> result = response.jsonPath().getList("", Product.class);

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    () -> assertThat(result.size()).isEqualTo(products.size()),
                    () -> assertThat(result.get(0))
                            .usingRecursiveComparison()
                            .ignoringFields("id", "price")
                            .isEqualTo(products.get(0)),
                    () -> assertThat(result.get(1))
                            .usingRecursiveComparison()
                            .ignoringFields("id", "price")
                            .isEqualTo(products.get(1))
            );
        }
    }
}
