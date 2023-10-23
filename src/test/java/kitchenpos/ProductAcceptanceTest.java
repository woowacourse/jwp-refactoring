package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import kitchenpos.ui.request.ProductCreateRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.ProductFixture.스키야키;
import static kitchenpos.step.ProductStep.PRODUCT_CREATE_REQUEST_스키야키;
import static kitchenpos.step.ProductStep.PRODUCT_CREATE_REQUEST_우동;
import static kitchenpos.step.ProductStep.상품_생성_요청;
import static kitchenpos.step.ProductStep.상품_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;

class ProductAcceptanceTest extends AcceptanceTest {

    @Nested
    class ProductTest {

        @Test
        void 상품을_생성한다() {
            final Product product = 스키야키();
            final ExtractableResponse<Response> response = 상품_생성_요청(PRODUCT_CREATE_REQUEST_스키야키);

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
    }

    @Nested
    class ProductQueryTest {

        @Test
        void 상품을_조회한다() {
            final List<ProductCreateRequest> requests = List.of(PRODUCT_CREATE_REQUEST_스키야키, PRODUCT_CREATE_REQUEST_우동);
            for (final ProductCreateRequest request : requests) {
                상품_생성_요청(request);
            }

            final ExtractableResponse<Response> response = 상품_조회_요청();
            final List<Product> result = response.jsonPath().getList("", Product.class);

            final List<Product> products = requests.stream()
                    .map(ProductCreateRequest::toProduct)
                    .collect(Collectors.toList());

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
