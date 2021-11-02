package kitchenpos.acceptance.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품 등록 성공")
    @Test
    void create() {
        ProductRequest chicken = new ProductRequest("강정치킨", BigDecimal.valueOf(17000));

        ResponseEntity<ProductResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/products",
                chicken,
                ProductResponse.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ProductResponse response = responseEntity.getBody();
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getName()).isEqualTo("강정치킨");
        assertThat(response.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(17000));
    }

    @DisplayName("상품 등록 실패 - 가격 부재")
    @Test
    void createByNullPrice() {
        ProductRequest chicken = new ProductRequest("강정치킨", null);

        ResponseEntity<ProductResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/products",
                chicken,
                ProductResponse.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("상품 등록 실패 - 가격 0 미만")
    @Test
    void createByNegativePrice() {
        ProductRequest chicken = new ProductRequest("강정치킨", BigDecimal.valueOf(-1));

        ResponseEntity<ProductResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/products",
                chicken,
                ProductResponse.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("상품 목록 조회")
    @Test
    void list() {
        Product chicken1 = new Product("강정치킨", BigDecimal.valueOf(17000));
        Product chicken2 = new Product("간장치킨", BigDecimal.valueOf(17000));

        productRepository.save(chicken1);
        productRepository.save(chicken2);

        ResponseEntity<List<Product>> responseEntity = testRestTemplate.exchange(
                "/api/products",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Product> response = responseEntity.getBody();
        assertThat(response).hasSize(2);
        assertThat(response)
                .extracting(Product::getName)
                .containsExactlyInAnyOrder("강정치킨", "간장치킨");
    }
}
