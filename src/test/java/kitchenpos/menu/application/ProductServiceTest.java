package kitchenpos.menu.application;

import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.repository.ProductRepository;
import kitchenpos.support.application.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static kitchenpos.support.Fixture.makeProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class ProductServiceTest {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceTest(final ProductService productService,
                              final ProductRepository productRepository
    ) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @Nested
    @ServiceTest
    class CreateTest {

        @DisplayName("상품을 추가한다")
        @ParameterizedTest
        @ValueSource(longs = {0, 1})
        void create(final long price) {
            assert price >= 0;

            final var request = makeProductRequest("탕수육", price);
            final var actual = productService.create(request);

            assertThat(actual.getId()).isPositive();
        }

        @DisplayName("가격은 음수가 아니어야 한다")
        @ParameterizedTest
        @ValueSource(longs = -1)
        void createWithNegativePrice(final long price) {
            assert price < 0;

            final var request = makeProductRequest("탕수육", price);
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("가격은 음수가 아니어야 합니다.");
        }
    }

    @DisplayName("상품을 전체 조회한다")
    @Test
    void list() {
        final var expectedSize = 4;
        saveProductsAsTimes(expectedSize);

        final var actual = productService.list();
        assertThat(actual).hasSize(expectedSize);
    }

    private void saveProductsAsTimes(final int times) {
        for (int i = 0; i < times; i++) {
            final var product = new Product("메뉴", new Price(BigDecimal.valueOf(4500)));
            productRepository.save(product);
        }
    }
}
