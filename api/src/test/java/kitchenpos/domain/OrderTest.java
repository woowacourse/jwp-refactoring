package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderedItem;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private static final OrderTable NOT_EMPTY_ORDER_TABLE = new OrderTable(3, false);
    private static final List<OrderLineItem> ORDER_LINE_ITEMS = List.of(new OrderLineItem(
        new OrderedItem(1L, "item", Price.from(BigDecimal.valueOf(1000L))), 1)
    );

    @DisplayName("주문을 처음 생성할 때, 주문 상태가 COOKING 으로 생성되고 주문 아이템이 함께 저장된다.")
    @Test
    void createDefault() {
        // given
        // when
        final Order order = Order.createDefault(NOT_EMPTY_ORDER_TABLE.getId(), ORDER_LINE_ITEMS);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        final Order order = Order.createDefault(NOT_EMPTY_ORDER_TABLE.getId(), ORDER_LINE_ITEMS);

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태를 변경할 때, 이미 완료된 주문이라면 예외가 발생한다.")
    @Test
    void changeOrderStatus_fail() {
        // given
        final Order order = Order.createDefault(NOT_EMPTY_ORDER_TABLE.getId(), ORDER_LINE_ITEMS);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when, then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 완료된 주문이라면 주문 상태를 변경할 수 없습니다.");
    }
}
