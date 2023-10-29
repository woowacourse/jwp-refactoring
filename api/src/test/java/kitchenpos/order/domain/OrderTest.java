package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    @DisplayName("주문이 정상적으로 생성된다.")
    void createOrder() {
        // given
        final Long orderTableId = 1L;
        final List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(), new OrderLineItem());

        // when
        final Order order = Order.ofCooking(orderTableId, orderLineItems);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(COOKING);
        assertThat(order.getOrderLineItems()).hasSize(2);
    }

    @Test
    @DisplayName("주문 상태를 정상적으로 업데이트한다.")
    void updateOrderStatus() {
        // given
        final Order order = Order.ofCooking(1L, Arrays.asList(new OrderLineItem(), new OrderLineItem()));

        // when
        order.updateOrderStatus(COMPLETION);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(COMPLETION);
    }

    @Test
    @DisplayName("주문 아이템 목록이 비어있는 경우 예외를 발생시킨다.")
    void throwExceptionIfOrderLineItemsIsEmpty() {
        // given
        final Long orderTableId = 1L;

        // then
        assertThatThrownBy(
                () -> Order.ofCooking(orderTableId, List.of())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 ID가 null인 경우 예외를 발생시킨다.")
    void throwExceptionIfOrderTableIdIsNull() {
        // given
        final List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(), new OrderLineItem());

        // then

        assertThatThrownBy(
                () -> Order.ofCooking(null, orderLineItems)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("주문 상태를 null로 업데이트하려 할 경우 예외가 발생한다.")
    void throwExceptionIfUpdateOrderStatusToNull() {
        // given
        final Order order = Order.ofCooking(1L, List.of(new OrderLineItem(), new OrderLineItem()));

        // then
        assertThatThrownBy(
                () -> order.updateOrderStatus(null)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("주문 아이템 목록이 null인 경우 주문을 생성하려 할 때 예외가 발생한다.")
    void throwExceptionIfOrderLineItemsIsNull() {
        // given
        final Long orderTableId = 1L;

        // then
        assertThatThrownBy(
                () -> Order.ofCooking(orderTableId, null)
        ).isInstanceOf(NullPointerException.class);
    }
}
