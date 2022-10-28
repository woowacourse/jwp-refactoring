package kitchenpos.e2e;

import static kitchenpos.support.ProductFixture.createProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ProductE2eTest extends E2eTest {

    @Test
    void create() {

        // given
        final ProductRequest requestBody = createProductRequest("양념 치킨", 10_000);

        // when
        final ExtractableResponse<Response> response = POST_요청("/api/products", requestBody);

        final Product 응답 = response.body().as(Product.class);

        // then
        assertAll(
                상태코드_검증(HttpStatus.CREATED, response),
                NOT_NULL_검증(응답),
                단일_검증(응답.getName(), "양념 치킨"),
                단일_검증(응답.getPrice(), BigDecimal.valueOf(10_000))
        );
    }

    @Test
    void list() {

        // given
        final ProductRequest 양념_치킨 = createProductRequest("양념 치킨", 10_000);
        final ProductRequest 간장_치킨 = createProductRequest("간장 치킨", 10_000);

        POST_요청("/api/products", 양념_치킨);
        POST_요청("/api/products", 간장_치킨);

        // when
        final ExtractableResponse<Response> response = GET_요청("/api/products");

        final List list = response.body().as(List.class);

        // then
        assertAll(
                리스트_검증(list, "id", 1, 2),
                리스트_검증(list, "name", 양념_치킨.getName(), 간장_치킨.getName()),
                리스트_검증(list, "price", 양념_치킨.getPrice().doubleValue(), 간장_치킨.getPrice().doubleValue())
        );
    }
}