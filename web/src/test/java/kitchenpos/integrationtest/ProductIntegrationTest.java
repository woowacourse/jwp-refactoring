package kitchenpos.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import kitchenpos.product.service.ProductDto;

class ProductIntegrationTest extends IntegrationTest {

    @Nested
    class create_success {

        @Test
        void min_price() {
            // given
            ProductDto expected = ProductFixture.computeDefaultProductDto(arg ->
                arg.setPrice(BigDecimal.valueOf(0L))
            );

            // when
            steps.createProduct(expected);
            ProductDto actual = sharedContext.getResponse().as(ProductDto.class);

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
            ProductDto productDto = ProductFixture.computeDefaultProductDto(ignored -> {});

            // when
            steps.createProduct(productDto);
            steps.createProduct(productDto);
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
            ProductDto expected = ProductFixture.computeDefaultProductDto(arg ->
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
    @DisplayName("제품 목록을 조회할 수 있다.")
    void listProducts_success() {
        // when
        List<ProductDto> actual = RestAssured.given().log().all()
                                             .get("/api/products")
                                             .then().log().all()
                                             .extract()
                                             .jsonPath().getList(".", ProductDto.class);

        // then
        assertThat(actual).isEmpty();
    }
}
