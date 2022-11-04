package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("상품의 가격은 음수일 수 없다.")
    @Test
    void productMustPositiveOrZero() {
        assertThatThrownBy(() -> new Product("이름", BigDecimal.ONE.negate()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격은 null일 수 없다.")
    @Test
    void productMustHasPrice() {
        assertThatThrownBy(() -> new Product("이름", null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
