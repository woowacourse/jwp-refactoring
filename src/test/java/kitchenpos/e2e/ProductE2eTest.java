package kitchenpos.e2e;

import static kitchenpos.e2e.E2eTest.AssertionPair.pair;
import static kitchenpos.support.ProductFixture.createProductRequest;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.support.ProductFixture.WrapProduct;
import kitchenpos.support.ProductFixture.WrapProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ProductE2eTest extends E2eTest {

    @Test
    void create() {

        // given
        final WrapProductRequest requestBody = createProductRequest("양념 치킨", 10_000);

        // when
        final ExtractableResponse<Response> 응답 = POST_요청("/api/products", requestBody);

        final WrapProduct 저장된_상품 = 응답.body().as(WrapProduct.class);

        // then
        assertAll(
                상태코드_검증(HttpStatus.CREATED, 응답),
                NOT_NULL_검증(저장된_상품),
                단일_검증(저장된_상품.name(), "양념 치킨"),
                단일_검증(저장된_상품.intPrice(), 10_000)
        );
    }

    @Test
    void list() {

        // given
        final WrapProductRequest 양념_치킨 = createProductRequest("양념 치킨", 10_000);
        final WrapProductRequest 간장_치킨 = createProductRequest("간장 치킨", 10_000);

        POST_요청("/api/products", 양념_치킨);
        POST_요청("/api/products", 간장_치킨);

        // when
        final ExtractableResponse<Response> response = GET_요청("/api/products");

        final List list = response.body().as(List.class);

        // then
        assertAll(
                리스트_검증(list,
                        pair("id", 1, 2),
                        pair("name", 양념_치킨.name(), 간장_치킨.name()),
                        pair("price", 양념_치킨.doublePrice(), 간장_치킨.doublePrice())
                )
        );
    }
}