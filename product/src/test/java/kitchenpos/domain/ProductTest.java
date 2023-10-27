package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    void 상품의_가격이_음수면_예외발생() {
        assertThatThrownBy(() -> new Product("product", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품의_가격이_존재하지_않으면_예외발생() {
        assertThatThrownBy(() -> new Product("product", null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
