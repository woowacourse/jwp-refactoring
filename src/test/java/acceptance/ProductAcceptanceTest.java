package acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@AcceptanceTest
class ProductAcceptanceTest {

    private static final long 후라이드_ID = 1L;
    private static final String 후라이드_이름 = "후라이드";
    private static final int 후라이드_가격 = 16000;

    private static final long 양념치킨_ID = 2L;
    private static final String 양념치킨_이름 = "양념치킨";
    private static final int 양념치킨_가격 = 16000;

    private static final long 반반치킨_ID = 3L;
    private static final String 반반치킨_이름 = "반반치킨";
    private static final int 반반치킨_가격 = 16000;

    private static final long 통구이_ID = 4L;
    private static final String 통구이_이름 = "통구이";
    private static final int 통구이_가격 = 16000;

    private static final long 간장치킨_ID = 5L;
    private static final String 간장치킨_이름 = "간장치킨";
    private static final int 간장치킨_가격 = 17000;

    private static final long 순살치킨_ID = 6L;
    private static final String 순살치킨_이름 = "순살치킨";
    private static final int 순살치킨_가격 = 17000;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void findProducts() {
        // act
        List<Product> products = getProducts();

        // assert
        assertThat(products)
                .extracting(Product::getId, Product::getName, p -> p.getPrice().intValueExact())
                .hasSize(6)
                .containsExactlyInAnyOrder(
                        tuple(후라이드_ID, 후라이드_이름, 후라이드_가격),
                        tuple(양념치킨_ID, 양념치킨_이름, 양념치킨_가격),
                        tuple(반반치킨_ID, 반반치킨_이름, 반반치킨_가격),
                        tuple(통구이_ID, 통구이_이름, 통구이_가격),
                        tuple(간장치킨_ID, 간장치킨_이름, 간장치킨_가격),
                        tuple(순살치킨_ID, 순살치킨_이름, 순살치킨_가격)
                );
    }

    @DisplayName("상품을 추가한다")
    @Test
    void saveProduct() {
        // arrange
        Product product = createProduct("옛날치킨", 10000);

        // act
        List<Product> products = getProducts();

        // assert
        assertThat(products)
                .extracting(Product::getId, Product::getName, p -> p.getPrice().intValueExact())
                .hasSize(7)
                .contains(
                        tuple(product.getId(), product.getName(), product.getPrice().intValueExact())
                );
    }

    private Product createProduct(final String name, final int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(product)
                .when().log().all()
                .post("/api/products")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(Product.class);
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
