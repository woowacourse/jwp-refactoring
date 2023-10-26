package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    void Product를_생성한다() {
        // given, when
        final Product product = new Product("상품", new Price(BigDecimal.valueOf(1000)));

        // then
        assertThat(product.getPrice().getValue()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    void Product_생성_시_입력된_가격이_null이면_예외가_발생한다() {
        // given
        final BigDecimal value = null;

        // when, then
        assertThatThrownBy(() -> new Product("상품", new Price(value)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Product_생성_시_입력된_가격이_0보다_작으면_예외가_발생한다() {
        // given
        final BigDecimal value = BigDecimal.valueOf(-1000);

        // when, then
        assertThatThrownBy(() -> new Product("상품", new Price(value)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}