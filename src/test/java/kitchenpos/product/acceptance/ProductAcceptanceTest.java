package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.common.exception.ExceptionResponse;
import kitchenpos.product.ui.request.ProductRequest;
import kitchenpos.product.ui.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("상품 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("POST /api/products - 상품을 생성할 때")
    @Nested
    class Post {

        @DisplayName("정상적인 상품은 저장에 성공한다.")
        @Test
        void success() {
            // given
            ProductRequest request = ProductReqeust를_생성한다("치즈버거", 4_500);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/products")
                .then().log().all()
                .statusCode(CREATED.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(CREATED.value());
            assertThat(response.header("Location")).isNotNull();
            assertThat(response.body()).isNotNull();
        }

        @DisplayName("이름이 Null인 경우 예외가 발생한다.")
        @Test
        void nameNullException() {
            // given
            ProductRequest request = ProductReqeust를_생성한다(null, 4_500);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/products")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("가격이 Null인 경우 예외가 발생한다.")
        @Test
        void priceNullException() {
            // given
            ProductRequest request = ProductReqeust를_생성한다("치킨버거", null);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/products")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("가격이 음수인 경우 예외가 발생한다.")
        @Test
        void priceNegativeException() {
            // given
            ProductRequest request = ProductReqeust를_생성한다("치킨버거", -1);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/products")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }
    }

    @DisplayName("GET /api/products - 모든 상품 목록을 조회한다.")
    @Test
    void findAll() {
        // when
        ExtractableResponse<Response> response = RestAssured.given()
            .log().all()
            .when().get("/api/products")
            .then().log().all()
            .statusCode(OK.value())
            .extract();

        List<ProductResponse> responses = response.jsonPath().getList(".", ProductResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(responses).isNotNull();
    }

    private ProductRequest ProductReqeust를_생성한다(String name, int price) {
        return ProductReqeust를_생성한다(name, BigDecimal.valueOf(price));
    }

    private ProductRequest ProductReqeust를_생성한다(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }
}
