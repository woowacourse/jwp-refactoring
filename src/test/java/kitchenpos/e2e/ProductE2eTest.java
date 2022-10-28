package kitchenpos.e2e;

import static kitchenpos.support.ProductFixture.createProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.support.DbTableCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductE2eTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DbTableCleaner tableCleaner;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        tableCleaner.clear();
    }

    @Test
    void create() {
        // given
        final ProductRequest productRequest = createProductRequest("양념 치킨", 10_000);

        // when
        final ExtractableResponse<Response> response = createProductRequestE2e(productRequest);

        final Product productResponse = response.body().as(Product.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(productResponse.getId()).isNotNull(),
                () -> assertThat(productResponse.getName()).isEqualTo("양념 치킨"),
                () -> assertThat(productResponse.getPrice()).isEqualTo(BigDecimal.valueOf(10_000))
        );
    }

    @Test
    void list() {
        // given
        final ProductRequest 양념_치킨 = createProductRequest("양념 치킨", 10_000);
        final ProductRequest 간장_치킨 = createProductRequest("간장 치킨", 10_000);

        createProductRequestE2e(양념_치킨);
        createProductRequestE2e(간장_치킨);

        // when
        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("/api/products")
                        .then().log().all()
                        .extract();

        final List list = response.body().as(List.class);

        // then
        assertAll(
                () -> assertThat(list).extracting("id").containsExactlyInAnyOrder(1, 2),
                () -> assertThat(list).extracting("name").containsExactlyInAnyOrder(양념_치킨.getName(), 간장_치킨.getName()),
                () -> assertThat(list).extracting("price").containsExactlyInAnyOrder(양념_치킨.getPrice().doubleValue(), 간장_치킨.getPrice().doubleValue())
        );
    }

    private ExtractableResponse<Response> createProductRequestE2e(final ProductRequest productRequest) {
        return RestAssured.given().log().all()
                .body(productRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/products")
                .then().log().all()
                .extract();
    }

}