package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Order 도메인 테스트")
class OrderTest {

    @DisplayName("주문의 상태 변경 시 주문이 완료된 상태면 안된다")
    @Test
    void changeStatusOrderIsCompletion() {
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1);
        final Order order = new Order( 1L, OrderStatus.COMPLETION, LocalDateTime.now(), List.of(orderLineItem));

        assertThatThrownBy(() -> order.changeStatus(OrderStatus.MEAL))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("주문이 완료된 상태입니다.");
    }
}
