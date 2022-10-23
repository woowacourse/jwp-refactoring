package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.application.request.ProductCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    void Product를_생성한다() {
        final ExtractableResponse<Response> actual = product_생성("고추바사삭치킨");

        assertThat(actual.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void Product의_목록을_조회할_수_있다() {
        product_생성("고추바사삭치킨");

        final ExtractableResponse<Response> actual = product_목록조회();

        assertThat(actual.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> product_생성(final String name) {
        final ProductCreateRequest request = new ProductCreateRequest(name, new BigDecimal(10000));

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/products")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> product_목록조회() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/products")
                .then().log().all()
                .extract();
    }
}
