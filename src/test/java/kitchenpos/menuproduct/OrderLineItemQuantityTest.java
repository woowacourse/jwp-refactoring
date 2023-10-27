package kitchenpos.menuproduct;

import kitchenpos.order.OrderLineItemQuantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemQuantityTest {
    @Test
    @DisplayName("수량이 음수이면 예외가 발생한다.")
    void validateQuantity() {
        assertThatThrownBy(() -> new OrderLineItemQuantity(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
