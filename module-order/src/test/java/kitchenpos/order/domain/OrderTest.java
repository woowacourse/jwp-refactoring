package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.exception.CannotChangeOrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Order 도메인 테스트")
class OrderTest {

    @DisplayName("[성공] OrderStatus가 COMPLETION가 아니라면 OrderStatus 변경")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COMPLETION"})
    void changeOrderStatus_ValidOrderStatus_ExceptionThrown(OrderStatus orderStatus) {
        // given
        List<OrderLineItem> orderLineItems = Collections
            .singletonList(new OrderLineItem(1L, 1L));
        Order order = new Order(1L, orderLineItems);

        // when
        order.changeOrderStatus(orderStatus.name());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus.name());
    }

    @DisplayName("[실패] OrderStatus가 COMPLETION이라면 OrderStatus 변경 불가")
    @Test
    void changeOrderStatus_OrderStatusCompletion_ExceptionThrown() {
        // given
        List<OrderLineItem> orderLineItems = Collections
            .singletonList(new OrderLineItem(1L, 1L));
        Order order = new Order(1L, orderLineItems);

        order.changeOrderStatus(OrderStatus.COMPLETION.name());

        // when
        // then
        Assertions.assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL.name()))
            .isInstanceOf(CannotChangeOrderStatus.class);
    }
}
