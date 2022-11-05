package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @DisplayName("주문 목록이 빈 경우 예외를 발생시킨다.")
    @Test
    void constructor_validateSize() {
        assertThatThrownBy(() -> new OrderLineItems(List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
