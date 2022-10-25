package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 새로 등록할 수 있다.")
    @Test
    void create() {
        // given
        final Product product = new Product("후라이드", BigDecimal.valueOf(16_000));

        // when
        final Product response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(product)
                .when().log().all()
                .post("/api/products")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(Product.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(product.getName());
        assertThat(response.getPrice().longValue()).isEqualTo(product.getPrice().longValue());
    }

    @DisplayName("상품 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().log().all()
                .get("/api/products")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        final List<Product> products = getProducts(response);

        // then
        assertThat(products)
                .hasSize(6)
                .filteredOn(it -> it.getId() != null)
                .extracting(Product::getName, product -> product.getPrice().longValue())
                .containsExactlyInAnyOrder(
                        tuple("후라이드", 16_000L),
                        tuple("양념치킨", 16_000L),
                        tuple("반반치킨", 16_000L),
                        tuple("통구이", 16_000L),
                        tuple("간장치킨", 17_000L),
                        tuple("순살치킨", 17_000L)
                );
    }

    private static List<Product> getProducts(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", Product.class);
    }
}
