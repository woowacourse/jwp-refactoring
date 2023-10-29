package kitchenpos;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemQuantityTest {
    @Test
    @DisplayName("수량이 음수이면 예외가 발생한다.")
    void validateQuantity() {
        Assertions.assertThatThrownBy(() -> new OrderLineItemQuantity(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
