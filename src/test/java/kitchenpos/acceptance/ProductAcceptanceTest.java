package kitchenpos.acceptance;

import static io.restassured.RestAssured.*;
import static kitchenpos.ui.ProductRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import kitchenpos.domain.Product;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductAcceptanceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * Feature: 상품 관리
     * <p>
     * Scenario: 상품을 관리한다.
     * <p>
     * When: 상품을 등록한다.
     * Then: 상품이 등록된다.
     * <p>
     * When: 상품의 목록을 조회한다.
     * Then: 저장되어 있는 상품의 목록이 반환된다.
     */
    @DisplayName("상품을 관리한다")
    @TestFactory
    Stream<DynamicTest> manageProduct() {
        final Product product = new Product();
        product.setName("마늘치킨");
        product.setPrice(BigDecimal.valueOf(18000));

        return Stream.of(
                dynamicTest(
                        "상품을 등록한다",
                        () -> {
                            final Product createdProduct = createProduct(product);
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
                            final List<Product> products = listProduct();
                            assertAll(
                                    assertThat(products)::isNotEmpty
                                    ,
                                    () -> assertThat(products)
                                            .usingComparatorForElementFieldsWithNames(
                                                    BigDecimal::compareTo,
                                                    "price")
                                            .usingElementComparatorOnFields("name", "price")
                                            .contains(product)
                            );
                        }
                )
        );
    }

    private Product createProduct(Product product) throws JsonProcessingException {
        final String request = objectMapper.writeValueAsString(product);

        // @formatter:off
        return
                given()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                        .post(PRODUCT_REST_API_URI)
                .then()
                        .log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().as(Product.class)
                ;
        // @formatter:on
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
