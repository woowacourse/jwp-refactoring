package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("Product 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("POST /api/products")
    @Nested
    class Post {

        @DisplayName("정상적인 Product는 저장에 성공한다.")
        @Test
        void success() {
            // given
            Product product = Product를_생성한다("치즈버거", 4_500);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when().post("/api/products")
                .then().log().all()
                .statusCode(CREATED.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(CREATED.value());
            assertThat(response.header("Location")).isNotNull();
            assertThat(response.body()).isNotNull();
        }

        @DisplayName("name이 Null인 경우 예외가 발생한다.")
        @Test
        void nameNullException() {
            // given
            Product menuGroup = Product를_생성한다(null, 4_500);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroup)
                .when().post("/api/products")
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("price가 Null인 경우 예외가 발생한다.")
        @Test
        void priceNullException() {
            // given
            Product menuGroup = Product를_생성한다("치킨버거", null);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroup)
                .when().post("/api/products")
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("price가 음수인 경우 예외가 발생한다.")
        @Test
        void priceNegativeException() {
            // given
            Product menuGroup = Product를_생성한다("치킨버거", -1);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroup)
                .when().post("/api/products")
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }
    }

    @DisplayName("GET /api/products - 모든 Product를 조회한다.")
    @Test
    void findAll() {
        // when
        ExtractableResponse<Response> response = RestAssured.given()
            .log().all()
            .when().get("/api/products")
            .then().log().all()
            .statusCode(OK.value())
            .extract();

        List<Product> products = response.jsonPath().getList(".", Product.class);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(products).isNotNull();
    }

    private Product Product를_생성한다(String name, int price) {
        return Product를_생성한다(name, BigDecimal.valueOf(price));
    }

    private Product Product를_생성한다(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }
}
