package kitchenpos.application.product;

import kitchenpos.application.ProductService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductQueryServiceTest extends ApplicationTestConfig {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @DisplayName("[SUCCESS] 모든 상품 목록을 조회한다.")
    @Test
    void success_findAll() {
        // given
        final List<Product> expected = new ArrayList<>();
        for (int productSaveCount = 1; productSaveCount <= 10; productSaveCount++) {
            final Product savedProduct = productRepository.save(new Product("테스트용 상품 이름", new Price("10000")));
            expected.add(savedProduct);
        }

        // when
        final List<Product> actual = productService.list();

        // then
        assertThat(actual)
                .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
