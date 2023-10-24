package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    void Product를_생성한다() {
        // given, when
        final Product product = new Product("상품", BigDecimal.valueOf(1000));

        // then
        assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    void Product_생성_시_입력된_가격이_null이면_예외가_발생한다() {
        // given
        final BigDecimal price = null;

        // when, then
        assertThatThrownBy(() -> new Product("상품", price))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Product_생성_시_입력된_가격이_0보다_작으면_예외가_발생한다() {
        // given
        final BigDecimal price = BigDecimal.valueOf(-1000);

        // when, then
        assertThatThrownBy(() -> new Product("상품", price))
                .isInstanceOf(IllegalArgumentException.class);
    }
}