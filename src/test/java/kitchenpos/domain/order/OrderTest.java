package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Order 도메인 테스트")
class OrderTest {

    @DisplayName("주문의 상태 변경 시 주문이 완료된 상태면 안된다")
    @Test
    void changeStatusOrderIsCompletion() {
        final OrderStatus invalidStatus = OrderStatus.COMPLETION;

        final OrderMenu orderMenu = new OrderMenu("후라이드 치킨 세트", BigDecimal.valueOf(15_000));
        final OrderLineItem orderLineItem = new OrderLineItem(orderMenu, 1);
        final Order order = new Order( 1L, invalidStatus, LocalDateTime.now(), List.of(orderLineItem));

        assertThatThrownBy(() -> order.changeStatus(OrderStatus.MEAL))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("주문이 완료된 상태입니다.");
    }
}
