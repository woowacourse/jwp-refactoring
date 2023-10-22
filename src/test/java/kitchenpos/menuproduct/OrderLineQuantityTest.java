package kitchenpos.menuproduct;

import kitchenpos.orderlineitem.OrderLineQuantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineQuantityTest {
    @Test
    @DisplayName("수량이 음수이면 예외가 발생한다.")
    void validateQuantity() {
        assertThatThrownBy(() -> new OrderLineQuantity(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
