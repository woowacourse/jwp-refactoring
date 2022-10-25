package acceptance;

import static fixture.ProductFixtures.간장치킨_상품;
import static fixture.ProductFixtures.반반치킨_상품;
import static fixture.ProductFixtures.순살치킨_상품;
import static fixture.ProductFixtures.양념치킨_상품;
import static fixture.ProductFixtures.통구이_상품;
import static fixture.ProductFixtures.후라이드_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import common.AcceptanceTest;
import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ProductAcceptanceTest extends AcceptanceTest {

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
                        tuple(후라이드_상품.id(), 후라이드_상품.이름(), 후라이드_상품.가격()),
                        tuple(양념치킨_상품.id(), 양념치킨_상품.이름(), 양념치킨_상품.가격()),
                        tuple(반반치킨_상품.id(), 반반치킨_상품.이름(), 반반치킨_상품.가격()),
                        tuple(통구이_상품.id(), 통구이_상품.이름(), 통구이_상품.가격()),
                        tuple(간장치킨_상품.id(), 간장치킨_상품.이름(), 간장치킨_상품.가격()),
                        tuple(순살치킨_상품.id(), 순살치킨_상품.이름(), 순살치킨_상품.가격())
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
