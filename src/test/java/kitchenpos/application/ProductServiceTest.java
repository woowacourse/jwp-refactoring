package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.SpringBootTestWithProfiles;
import kitchenpos.application.dto.request.ProductRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTestWithProfiles
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 정상 등록")
    void create() {
        ProductRequest productRequest = new ProductRequest("product", BigDecimal.valueOf(1000));

        ProductResponse saved = productService.create(productRequest);
        assertNotNull(saved.getId());
    }

    @Test
    @DisplayName("상품 등록 실패 :: 상품 가격 null")
    void createWithNullPrice() {
        ProductRequest nullPriceRequest = new ProductRequest("product", null);

        assertThatThrownBy(() -> productService.create(nullPriceRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 등록 실패 :: 상품 가격 음수")
    void createWithNegativePrice() {
        ProductRequest negativePriceRequest = new ProductRequest("product", BigDecimal.valueOf(-1000));

        assertThatThrownBy(() -> productService.create(negativePriceRequest)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 정상 조회")
    void list() {
        productService.create(new ProductRequest("productA", BigDecimal.valueOf(1000)));
        productService.create(new ProductRequest("productB", BigDecimal.valueOf(1000)));
        productService.create(new ProductRequest("productC", BigDecimal.valueOf(1000)));

        List<ProductResponse> products = productService.list();
        assertThat(products).hasSize(3);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }
}
