package kitchenpos.domain.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductPriceTest {
    @Test
    void 상품_가격이_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> new ProductPrice(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_가격이_0원_보다_작으면_예외가_발생한다() {
        assertThatThrownBy(() -> new ProductPrice(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_가격이_1000조_이상이면_예외가_발생한다() {
        assertThatThrownBy(() -> new ProductPrice(BigDecimal.valueOf(Math.pow(10, 17))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
