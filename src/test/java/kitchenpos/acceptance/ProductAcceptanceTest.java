package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest{
    @DisplayName("POST /api/products")
    @Test
    void create() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "강정치킨");
        params.put("price", 17000.0);

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/products")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.body()).isNotNull();
    }

    @DisplayName("GET /api/products")
    @Test
    void list() {
        // given

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        List<Product> products = convertBodyToList(response, Product.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(products).isNotNull();
        assertThat(products).isNotEmpty();
    }
}
