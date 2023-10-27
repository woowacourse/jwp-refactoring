package kitchenpos.common;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        // given
        OrderTable orderTable = new OrderTable(1L, 2L, 5, false);
        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 5));

        // when
        Order order = Order.create(orderTable, orderLineItems);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("주문 항목이 포함되어 있지 않은 주문을 생성하면 예외가 발생한다.")
    @Test
    void create_ItemEmpty_ExceptionThrown() {
        // given
        OrderTable orderTable = new OrderTable(1L, 2L, 5, false);
        List<OrderLineItem> orderLineItems = Collections.emptyList();
        
        // when, then
        assertThatThrownBy(() -> Order.create(orderTable, orderLineItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어 있는 테이블에 주문을 생성하면 예외가 발생한다.")
    @Test
    void create_TableEmpty_ExceptionThrown() {
        // given
        OrderTable orderTable = new OrderTable(1L, 2L, 5, true);
        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 5));

        // when, then
        assertThatThrownBy(() -> Order.create(orderTable, orderLineItems))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
