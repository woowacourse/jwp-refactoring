package kitchenpos.acceptance;

import static io.restassured.RestAssured.*;
import static kitchenpos.ui.ProductRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import kitchenpos.domain.Product;

class ProductAcceptanceTest extends AcceptanceTest {
    /*
     * Feature: 상품 관리
     *
     * Scenario: 상품을 관리한다.
     *
     * When: 상품을 등록한다.
     * Then: 상품이 등록된다.
     *
     * Given: 상품이 등록되어 있다.
     * When: 상품의 목록을 조회한다.
     * Then: 저장되어 있는 상품의 목록이 반환된다.
     */
    @DisplayName("상품을 관리한다")
    @TestFactory
    Stream<DynamicTest> manageProduct() {
        return Stream.of(
                dynamicTest(
                        "상품을 등록한다",
                        () -> {
                            // When
                            final Product product = new Product();
                            product.setName("마늘치킨");
                            product.setPrice(BigDecimal.valueOf(18000));

                            final Product createdProduct = createProduct(product);

                            // Then
                            assertAll(
                                    () -> assertThat(createdProduct)
                                            .extracting(Product::getName)
                                            .isEqualTo(product.getName())
                                    ,
                                    () -> assertThat(createdProduct)
                                            .extracting(Product::getPrice)
                                            .usingComparator(BigDecimal::compareTo)
                                            .isEqualTo(product.getPrice())
                            );
                        }
                ),
                dynamicTest(
                        "상품의 목록을 조회한다",
                        () -> {
                            // Given
                            final Product product = new Product();
                            product.setName("파닭치킨");
                            product.setPrice(BigDecimal.valueOf(18000));

                            final Product createdProduct = createProduct(product);

                            // When
                            final List<Product> products = listProduct();

                            // Then
                            assertAll(
                                    assertThat(products)::isNotEmpty
                                    ,
                                    () -> assertThat(products)
                                            .extracting(Product::getId)
                                            .contains(createdProduct.getId())
                            );
                        }
                )
        );
    }

    private List<Product> listProduct() {
        // @formatter:off
        return
                given()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                        .get(PRODUCT_REST_API_URI)
                .then()
                        .log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .jsonPath().getList(".", Product.class)
                ;
        // @formatter:on
    }
}
