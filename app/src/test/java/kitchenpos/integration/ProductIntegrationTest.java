package kitchenpos.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import kitchenpos.product.application.dto.request.ProductCreateRequest;
import kitchenpos.product.application.dto.response.ProductResponse;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProductIntegrationTest extends IntegrationTest {

    @Test
    void 상품_생성을_요청한다() {
        // given
        final ProductCreateRequest productCreateRequest = new ProductCreateRequest("상품", BigDecimal.valueOf(1000));
        final HttpEntity<ProductCreateRequest> request = new HttpEntity<>(productCreateRequest);

        // when
        final ResponseEntity<ProductResponse> response = createProduct(request);
        final ProductResponse createdProduct = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().get("Location"))
                        .contains("/api/products/" + createdProduct.getId()),
                () -> assertThat(createdProduct.getName()).isEqualTo("상품"),
                () -> assertThat(createdProduct.getPrice().intValue()).isEqualTo(1000)
        );
    }

    @Test
    void 모든_상품_목록을_조회한다() {
        // given
        final ProductCreateRequest productCreateRequest = new ProductCreateRequest("상품", BigDecimal.valueOf(1000));
        final HttpEntity<ProductCreateRequest> request = new HttpEntity<>(productCreateRequest);

        createProduct(request);
        createProduct(request);

        // when
        final ResponseEntity<ProductResponse[]> response = testRestTemplate
                .getForEntity("/api/products", ProductResponse[].class);
        final List<ProductResponse> products = Arrays.asList(response.getBody());

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(products).hasSize(2)
        );
    }

    private ResponseEntity<ProductResponse> createProduct(HttpEntity<ProductCreateRequest> request) {
        return testRestTemplate
                .postForEntity("/api/products", request, ProductResponse.class);
    }
}
