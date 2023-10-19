package kitchenpos.domain.menuproduct;

import kitchenpos.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuantityTest {
    @Test
    @DisplayName("수량이 음수이면 예외가 발생한다.")
    void validateQuantity() {
        assertThatThrownBy(() -> new Quantity(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
