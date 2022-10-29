package kitchenpos.application;

import kitchenpos.application.request.product.ProductRequest;
import kitchenpos.dao.ProductDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static kitchenpos.fixture.ProductFixture.newProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class ProductServiceTest {

    private final ProductService productService;
    private final ProductDao productDao;

    @Autowired
    public ProductServiceTest(final ProductService productService, final ProductDao productDao) {
        this.productService = productService;
        this.productDao = productDao;
    }

    @Nested
    @ServiceTest
    class CreateTest {

        @DisplayName("상품을 추가한다")
        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        void create(final int price) {
            final var request = new ProductRequest("탕수육", BigDecimal.valueOf(price));
            final var actual = productService.create(request);

            assertThat(actual.getId()).isPositive();
        }

        @DisplayName("가격은 음수가 아니어야 한다")
        @Test
        void createWithNegativePrice() {
            final var negativePrice = -1;

            final var request = new ProductRequest("탕수육", BigDecimal.valueOf(negativePrice));

            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("상품을 전체 조회한다")
    @Test
    void list() {
        final var expectedSize = 4;
        for (int i = 0; i < expectedSize; i++) {
            final var product = newProduct("메뉴", 4500);
            productDao.save(product);
        }

        final var actual = productService.list();
        assertThat(actual).hasSize(expectedSize);
    }
}
