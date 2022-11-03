package kitchenpos.acceptance;

import io.restassured.response.ValidatableResponse;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.support.RequestBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("상품 관련 api")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        // given, when
        final ProductRequest request = RequestBuilder.ofProduct();
        final ValidatableResponse response = post("/api/products", request);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.notNullValue());
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // given, when
        final ValidatableResponse response = get("/api/products");

        // then
        response.statusCode(HttpStatus.OK.value());
    }
}
