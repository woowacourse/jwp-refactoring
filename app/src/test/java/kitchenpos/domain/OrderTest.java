package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import kitchenpos.common.OrderStatus;
import kitchenpos.order.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        final Order order = new Order(1L, OrderStatus.COOKING.name());

        // when & then
        assertDoesNotThrow(() -> order.changeOrderStatus(OrderStatus.MEAL.name()));
    }

    @DisplayName("주문의 상태가 이미 완료되었으면 예외 처리한다.")
    @Test
    void changeOrderStatus_FailWhenStatusAlreadyCompletion() {
        // given
        final Order order = new Order(1L, OrderStatus.COMPLETION.name());

        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COMPLETION.name()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 완료된 주문입니다.");
    }
}
