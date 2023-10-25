package kitchenpos.menu.application;

import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.menu.domain.Product;
import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        final List<Product> savedProducts = new ArrayList<>();
        for (int productSaveCount = 1; productSaveCount <= 10; productSaveCount++) {
            final Product savedProduct = productRepository.save(new Product(new Name("테스트용 상품명"), Price.from("10000")));
            savedProducts.add(savedProduct);
        }

        // when
        final List<ProductResponse> actual = productService.list();

        // then
        final List<ProductResponse> expected = savedProducts.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());

        assertThat(actual)
                .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
