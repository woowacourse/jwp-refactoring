package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderTest {

    private final MenuGroup menuGroup = MenuGroup.create("두마리메뉴");
    private final Menu menu = Menu.create("두마리메뉴 - 후1양1", BigDecimal.valueOf(32000L), menuGroup);
    private final OrderLineItem orderLineItem1 = OrderLineItem.create(menu, 1L);
    private final OrderLineItem orderLineItem2 = OrderLineItem.create(menu, 1L);
    private final OrderTable orderTable = OrderTable.create(1, false);

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void order() {
        // then
        assertDoesNotThrow(() -> Order.create(orderTable));
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus(OrderStatus orderStatus) {
        // given
        Order order = Order.create(orderTable);

        // when
        order.changeOrderStatus(orderStatus);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }

    @DisplayName("주문 상태 변경 시, 이미 완료된 주문이면 예외가 발생한다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus_FailWithCompletionStatus(OrderStatus orderStatus) {
        // given
        Order order = Order.create(orderTable);

        order.changeOrderStatus(COMPLETION);

        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(orderStatus))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 완료된 주문은 변경할 수 없습니다.");
    }

    @DisplayName("주문 항목을 변경할 수 있다.")
    @Test
    void updateOrderLineItems() {
        // given
        Order order = Order.create(orderTable);

        // when
        order.updateOrderLineItems(List.of(orderLineItem1, orderLineItem2));

        // then
        assertDoesNotThrow(() -> order.updateOrderLineItems(List.of(orderLineItem1, orderLineItem2)));
    }

    @DisplayName("주문 항목 변경 시, 주문 항복이 0개 이하인 경우 예외가 발생한다.")
    @Test
    void updateOrderLineItems_FailWithInvalidOrderLineItemsSize() {
        // given
        Order order = Order.create(orderTable);

        // then
        assertThatThrownBy(() -> order.updateOrderLineItems(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목은 1개 이상이어야 합니다.");
    }
}
