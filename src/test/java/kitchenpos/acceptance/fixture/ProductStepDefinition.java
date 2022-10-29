package kitchenpos.acceptance.fixture;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.springframework.http.HttpStatus;

public class ProductStepDefinition {

    public static long 상품을_생성한다(
        final String name,
        final int price) {
        Product product = new Product(name, BigDecimal.valueOf(price));

        return RestAssured.given().log().all()
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(product)
            .when().log().all()
            .post("/api/products")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract().body().jsonPath().getLong("id");
    }

    public static List<Product> 상품_목록을_조회한다() {
        return RestAssured.given().log().all()
            .when().log().all()
            .get("/api/products")
            .then().log().all()
            .extract().body().jsonPath().getList(".", Product.class);
    }
}
