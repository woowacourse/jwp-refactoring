package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    void 프로덕트를_생성한다() {
        // given
        Product product = new Product("김치찌개", new BigDecimal("10000.00"));

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
    void 가격이_음수인_프로덕트_생성_요청시_예외_발생() {
        // given
        Product product = new Product("김치찌개", new BigDecimal("-3"));

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
    void 프로덕트_전체_목록을_조회한다() {
        // given
        Product product = new Product("김치찌개", new BigDecimal("10000.0"));

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
                .isEqualTo(List.of(product));
    }
}
