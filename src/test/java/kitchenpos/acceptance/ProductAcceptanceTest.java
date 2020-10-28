package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ProductAcceptanceTest extends AcceptanceTest {
    /**
     * Feature: 상품을 관리한다. Scenario: 상품을 여러개 등록하고, 상품 목록을 조회한다.
     *
     * When 상품을 등록한다. Then 상품이 등록되었다.
     *
     * When 상품 목록을 조회한다. Then 상품 목록이 조회된다.
     */
    @Test
    @DisplayName("상품 관리")
    void manageProduct() {
        createProduct("후라이드 치킨", 9_000);
        createProduct("간장 치킨", 10_000);

        List<Product> products = findProducts();
        assertThat(doesProductExistInProducts("후라이드 치킨", products)).isTrue();
        assertThat(doesProductExistInProducts("간장 치킨", products)).isTrue();
    }

    private List<Product> findProducts() {
        return given()
        .when()
            .get("/api/products")
        .then()
            .statusCode(HttpStatus.OK.value())
            .log().all()
            .extract()
            .jsonPath()
            .getList("", Product.class);
    }

    private boolean doesProductExistInProducts(String productName, List<Product> products) {
        return products.stream()
            .anyMatch(product -> product
                .getName()
                .equals(productName));
    }
}
