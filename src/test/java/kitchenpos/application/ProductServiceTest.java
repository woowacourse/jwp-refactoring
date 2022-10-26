package kitchenpos.application;

import static kitchenpos.support.ProductFixture.PRODUCT_PRICE_10000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Test
    void 제품을_저장한다() {
        // given
        final Product product = PRODUCT_PRICE_10000.생성();

        // when
        final Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }

    @Test
    void 제품을_저장할_때_가격이_음수이면_예외를_발생한다() {
        // given
        final Product product = new Product("제품명", new BigDecimal(-1));

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 제품을_저장할_때_가격이_null이면_예외를_발생한다() {
        // given
        final Product product = new Product("제품명", null);

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
