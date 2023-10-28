package kitchenpos.domain;

import static kitchenpos.exception.ExceptionType.ALREADY_COMPLETION_ORDER;
import static kitchenpos.exception.ExceptionType.DUPLICATED_ORDER_LINE_ITEM;
import static kitchenpos.exception.ExceptionType.EMPTY_ORDER_LINE_ITEMS;
import static kitchenpos.exception.ExceptionType.EMPTY_ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Order.Builder;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderFixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문을 생성할 수 있다.")
    void create() {
        // given & when & then
        assertDoesNotThrow(OrderFixture.ORDER_1::toEntity);
    }

    @Test
    @DisplayName("주문 상태 변화 성공")
    void change_order_status_success() {
        // given
        OrderLineItem orderLineItem = OrderLineItemFixture.ORDER_LINE_ITEM_1.toEntity();

        // when
        Order order = new Builder()
            .orderLineItems(List.of(orderLineItem))
            .orderStatus(OrderStatus.COOKING)
            .build();

        // then
        assertDoesNotThrow(() -> order.changeOrderStatus(OrderStatus.MEAL));
    }

    @Test
    @DisplayName("주문 상태 변화 실패 - 이미 완료된 주문")
    void change_order_status_fail() {
        // given
        OrderLineItem orderLineItem = OrderLineItemFixture.ORDER_LINE_ITEM_1.toEntity();

        // when
        Order order = new Builder()
            .orderLineItems(List.of(orderLineItem))
            .orderStatus(OrderStatus.COMPLETION)
            .build();

        // then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COMPLETION))
            .hasMessageContaining(ALREADY_COMPLETION_ORDER.getMessage());
    }
}
