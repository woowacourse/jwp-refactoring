package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.Product;
import kitchenpos.supports.IntegrationTest;
import kitchenpos.supports.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("상품 서비스 테스트")
@IntegrationTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    @DisplayName("상품을 생성할 때")
    class Create {

        @DisplayName("정상적으로 생성할 수 있다")
        @ParameterizedTest
        @ValueSource(ints = {0, 10000})
        void createProduct(int price) {
            // given
            final Product product = new Product("알리오 올리오", new Price(BigDecimal.valueOf(price)));

            // when
            final Product savedProduct = productService.create(product);

            // then
            assertSoftly(softly -> {
                assertThat(savedProduct.getId()).isPositive();
                assertThat(savedProduct.getName()).isEqualTo(product.getName());
            });
        }
    }

    @DisplayName("상품 목록을 조회할 수 있다")
    @Test
    void findAllProducts() {
        // given
        final Product product1 = productService.create(ProductFixture.create());
        final Product product2 = productService.create(ProductFixture.create());

        // when
        final List<Product> list = productService.list();

        // then
        assertSoftly(softly -> {
            assertThat(list).hasSize(2);
            assertThat(list)
                    .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(product1, product2));
        });
    }
}
