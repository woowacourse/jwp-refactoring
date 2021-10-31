package kitchenpos.acceptance;

import kitchenpos.domain.Product;
import kitchenpos.ui.request.ProductRequest;
import kitchenpos.ui.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 관련 기능")
class ProductAcceptanceTest extends AcceptanceTest {

    Product 후라이드치킨;
    Product 양념치킨;

    @BeforeEach
    void setUp() {
        후라이드치킨 = new Product.Builder()
                .name("후라이드치킨")
                .price(BigDecimal.valueOf(15000))
                .build();
        후라이드치킨 = productRepository.save(후라이드치킨);

        양념치킨 = new Product.Builder()
                .name("양념치킨")
                .price(BigDecimal.valueOf(16000))
                .build();
        양념치킨 = productRepository.save(양념치킨);
    }

    @DisplayName("등록된 전체 상품들을 반환한다")
    @Test
    void getProducts() {
        // when
        ResponseEntity<ProductResponse[]> responseEntity = testRestTemplate.getForEntity("/api/products", ProductResponse[].class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(2);
    }

    @DisplayName("새로운 상품을 등록한다")
    @Test
    void registerProduct() {
        // given
        ProductRequest 강정치킨 = new ProductRequest();
        강정치킨.setName("강정치킨");
        강정치킨.setPrice(BigDecimal.valueOf(17000));

        // when
        ResponseEntity<ProductResponse> responseEntity = testRestTemplate.postForEntity("/api/products", 강정치킨, ProductResponse.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ProductResponse 응답된_상품 = responseEntity.getBody();
        assertThat(응답된_상품.getName()).isEqualTo("강정치킨");
        assertThat(응답된_상품.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(17000));
    }

    @DisplayName("새로운 상품을 등록할 때, 가격이 없으면 안된다.")
    @Test
    void cannotRegisterProductWhenNoPrice() {
        // given
        ProductRequest 강정치킨 = new ProductRequest();
        강정치킨.setName("강정치킨");

        // when
        ResponseEntity<ProductResponse> responseEntity = testRestTemplate.postForEntity("/api/products", 강정치킨, ProductResponse.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("새로운 상품을 등록할 때, 가격이 음수이면 안된다.")
    @Test
    void cannotRegisterProductWhenNegativePrice() {
        // given
        ProductRequest 강정치킨 = new ProductRequest();
        강정치킨.setName("강정치킨");
        강정치킨.setPrice(BigDecimal.valueOf(-1000L));

        // when
        ResponseEntity<ProductResponse> responseEntity = testRestTemplate.postForEntity("/api/products", 강정치킨, ProductResponse.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
