package acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.Application;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = Application.class
)
public class ProductAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void findProducts() {
        long productId1 = createProduct("후라이드", 19000);
        long productId2 = createProduct("돼지국밥", 9000);
        long productId3 = createProduct("피자", 31000);
        long productId4 = createProduct("수육", 25000);

        List<Product> products = getProducts();

        assertThat(products).extracting(Product::getId, Product::getName, p -> p.getPrice().intValueExact())
                .containsExactlyInAnyOrder(
                        tuple(productId1, "후라이드", 19000),
                        tuple(productId2, "돼지국밥", 9000),
                        tuple(productId3, "피자", 31000),
                        tuple(productId4, "수육", 25000)
                );
    }

    public static long createProduct(String name, int price) {
        Product product = givenProduct(name, price);
        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(product)
                .when().log().all()
                .post("/api/products")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");
    }

    public static Product givenProduct(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }

    private List<Product> getProducts() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/products")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", Product.class);
    }
}
