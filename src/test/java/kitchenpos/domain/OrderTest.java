package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("OrderLineItem 목록은 빈 목록이 될 수 없다")
    @Test
    void orderLineItemsIsEmpty_throwsException() {
        final List<OrderLineItem> invalidOrderLineItems = Collections.emptyList();

        assertThatThrownBy(
                () -> new Order(null, 1L, OrderStatus.COOKING, LocalDateTime.now(), invalidOrderLineItems)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상태가 COMPLETION이면, 상태를 변경할 수 없다")
    @Test
    void orderStatusIsCompletion_changeStatus_throwsException() {
        final var order = new Order(1L, 1L, OrderStatus.COMPLETION, LocalDateTime.now());

        assertThatThrownBy(
                () -> order.changeStatus(OrderStatus.COOKING)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
