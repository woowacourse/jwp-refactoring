package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.application.request.ProductCreateRequest;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    void 상품을_생성한다() {
        // given
        ProductCreateRequest product = new ProductCreateRequest("김치찌개", BigDecimal.valueOf(10000));

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(product)

                .when()
                .post("/api/products")

                .then()
                .log().all()
                .extract();

        // then
        Product actual = response.body().as(Product.class);

        assertThat(actual).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(product);
    }

    @Test
    void 가격이_음수인_상품_생성_요청시_예외_발생() {
        // given
        ProductCreateRequest product = new ProductCreateRequest("김치찌개", BigDecimal.valueOf(-3));

        ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(product)

                .when()
                .post("/api/products")

                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void 상품_전체_목록을_조회한다() {
        // given
        ProductCreateRequest product = new ProductCreateRequest("김치찌개", BigDecimal.valueOf(10000));

        RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(product)

                .when()
                .post("/api/products")

                .then()
                .log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(JSON)

                .when()
                .get("/api/products")

                .then()
                .log().all()
                .extract();

        // then
        List<Product> actual = response.jsonPath().getList("", Product.class);

        assertThat(actual).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(List.of(Product.of("김치찌개", new BigDecimal("10000.0"))));
    }
}
