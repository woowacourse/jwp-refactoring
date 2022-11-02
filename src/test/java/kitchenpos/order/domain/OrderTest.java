package kitchenpos.order.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @DisplayName("주문 상태를 변경한다")
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void updateOrderStatus(final OrderStatus expectedOrderStatus) {
        final var order = new Order(1L, makeSingleOrderLineItems());
        order.updateOrderStatus(expectedOrderStatus);

        assertThat(order.getOrderStatus()).isEqualByComparingTo(expectedOrderStatus);
    }

    @DisplayName("결제가 완료된 주문은 주문 상태를 변경할 수 없다")
    @Test
    void updateOrderStatusWithAlreadyCompletedOrder() {
        final var completedOrder = makeCompletedOrder();

        assertThatThrownBy(() -> completedOrder.updateOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 결제 완료된 주문입니다.");
    }

    private Order makeCompletedOrder() {
        final var order = new Order(1L, makeSingleOrderLineItems());
        order.updateOrderStatus(OrderStatus.COMPLETION);
        return order;
    }

    private List<OrderLineItem> makeSingleOrderLineItems() {
        return List.of(new OrderLineItem(1L, 10));
    }
}
