package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductIntegrationTest extends IntegrationTest {

    @Nested
    class create_success {

        @Test
        void min_price() {
            // given
            Product expected = ProductFixture.computeDefaultMenu(arg ->
                arg.setPrice(BigDecimal.valueOf(0L))
            );

            // when
            steps.createProduct(expected);
            Product actual = sharedContext.getResponse().as(Product.class);

            // then
            assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice().longValue()).isEqualTo(expected.getPrice().longValue())
            );
        }

        @Test
        void duplicate_name() {
            // given
            Product product = ProductFixture.computeDefaultMenu(ignored -> {});

            // when
            steps.createProduct(product);
            steps.createProduct(product);
            ExtractableResponse<Response> response = sharedContext.getResponse();

            // then
            assertThat(response.statusCode()).isEqualTo(201);
        }
    }

    @Nested
    class create_failure {

        @Test
        void price_lower_than_0() {
            // given
            Product expected = ProductFixture.computeDefaultMenu(arg ->
                arg.setPrice(BigDecimal.valueOf(-1L))
            );

            // when
            steps.createProduct(expected);
            ExtractableResponse<Response> response = sharedContext.getResponse();

            // then
            assertThat(response.statusCode()).isEqualTo(500);
        }
    }

    @Test
    void listProducts_success() {
        // when
        List<Product> actual = RestAssured.given().log().all()
                                          .get("/api/products")
                                          .then().log().all()
                                          .extract()
                                          .jsonPath().getList(".", Product.class);

        // then
        assertThat(actual).isEmpty();
    }
}
