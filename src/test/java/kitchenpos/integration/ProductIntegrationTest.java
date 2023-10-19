package kitchenpos.integration;

import static kitchenpos.integration.steps.ProductStep.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.supports.IntegrationTest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("상품 통합 테스트")
@IntegrationTest
public class ProductIntegrationTest {

    @Nested
    @DisplayName("상품을 생성할 때")
    class Create {

        @DisplayName("정상적으로 생성할 수 있다")
        @Test
        void createProduct() throws JSONException {
            // given
            JSONObject request = new JSONObject()
                    .put("name", "알리오 올리오")
                    .put("price", 10000);

            // when
            final ExtractableResponse<Response> response = 상품_생성_요청(request.toString());

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }

        @DisplayName("가격이 올바르지 않으면 상품을 생성할 수 없다")
        @Test
        void throwExceptionWhenPriceIsLowerThanZero() throws JSONException {
            // given
            JSONObject request = new JSONObject()
                    .put("name", "알리오 올리오")
                    .put("price", -999);

            // when
            final ExtractableResponse<Response> response = 상품_생성_요청(request.toString());

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }
}
