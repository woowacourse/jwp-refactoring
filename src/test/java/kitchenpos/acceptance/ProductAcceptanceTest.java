package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ProductAcceptanceTest extends AcceptanceTest {

    public static Long createProduct(String name, int price) {
        ProductRequest product = new ProductRequest(name, BigDecimal.valueOf(price));
        String location = RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(product)
                .when().log().all()
                .post("/api/products")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");

        return Long.parseLong(location.split("/api/products/")[1]);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void findProducts() {
        long productId1 = createProduct("후라이드", 19000);
        long productId2 = createProduct("돼지국밥", 9000);
        long productId3 = createProduct("피자", 31000);
        long productId4 = createProduct("수육", 25000);

        List<ProductResponse> products = getProducts();

        assertThat(products).extracting(ProductResponse::getId, ProductResponse::getName,
                        p -> p.getPrice().intValueExact())
                .containsExactlyInAnyOrder(
                        tuple(productId1, "후라이드", 19000),
                        tuple(productId2, "돼지국밥", 9000),
                        tuple(productId3, "피자", 31000),
                        tuple(productId4, "수육", 25000)
                );
    }

    private List<ProductResponse> getProducts() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/products")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", ProductResponse.class);
    }
}
