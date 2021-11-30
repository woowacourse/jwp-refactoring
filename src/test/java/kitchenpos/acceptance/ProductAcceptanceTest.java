package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.product.service.ProductRequest;
import kitchenpos.product.service.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductAcceptanceTest extends AcceptanceTest {

    public static final ProductRequest 강정치킨 = new ProductRequest("강정치킨", BigDecimal.valueOf(17000));
    public static final ProductRequest 양념치킨 = new ProductRequest("양념치킨", BigDecimal.valueOf(16000));
    public static final ProductRequest 간장치킨 = new ProductRequest("간장치킨", BigDecimal.valueOf(18000));

    public static ExtractableResponse<Response> PRODUCT_생성_요청(ProductRequest productRequest) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/api/products")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> PRODUCT_전체_조회_요청() {
        return RestAssured.given()
                .when()
                .get("/api/products")
                .then()
                .extract();
    }

    @Test
    void save() {
        ExtractableResponse<Response> response = PRODUCT_생성_요청(강정치킨);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    void findAll() {
        PRODUCT_생성_요청(강정치킨);
        PRODUCT_생성_요청(양념치킨);
        PRODUCT_생성_요청(간장치킨);

        ExtractableResponse<Response> response = PRODUCT_전체_조회_요청();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("", ProductResponse.class)).hasSize(3);
    }
}
