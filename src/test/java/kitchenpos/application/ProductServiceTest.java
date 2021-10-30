package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.SpringBootTestWithProfiles;
import kitchenpos.domain.Product;
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
        Product product = new Product("product", BigDecimal.valueOf(1000));

        Product saved = productService.create(product);
        assertNotNull(saved.getId());
    }

    @Test
    @DisplayName("상품 등록 실패 :: 상품 가격 null")
    void createWithNullPrice() {
        Product product = new Product("product", null);

        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 등록 실패 :: 상품 가격 음수")
    void createWithNegativePrice() {
        Product product = new Product("product", BigDecimal.valueOf(-1000));

        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 정상 조회")
    void list() {
        productService.create(new Product("productA", BigDecimal.valueOf(1000)));
        productService.create(new Product("productB", BigDecimal.valueOf(1000)));
        productService.create(new Product("productC", BigDecimal.valueOf(1000)));

        List<Product> products = productService.list();
        assertThat(products).hasSize(3);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }
}
