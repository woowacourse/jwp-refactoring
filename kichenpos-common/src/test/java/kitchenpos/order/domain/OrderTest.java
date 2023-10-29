package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @ParameterizedTest(name = "주문 상태가 {0}일 때")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("완료되지 않은 주문인지 판단할 때 완료되지 않은 주문이면 true를 반환한다")
    void isNotComplete_true(final OrderStatus orderStatus) {
        // given
        final OrderTable orderTable = new OrderTable(4, false);
        final Long menuId = 1L;
        final OrderLineItem orderLineItem = new OrderLineItem(menuId, 2);
        final Order order = new Order(orderTable.getId(), orderStatus, new OrderLineItems(List.of(orderLineItem)));

        // when
        final boolean actual = order.isNotComplete();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("완료되지 않은 주문인지 판단할 때 완료된 주문이면 false를 반환한다")
    void isNotComplete_false() {
        // given
        final OrderTable orderTable = new OrderTable(4, false);
        final Long menuId = 1L;
        final OrderLineItem orderLineItem = new OrderLineItem(menuId, 2);
        final Order completeOrder = new Order(orderTable.getId(), OrderStatus.COMPLETION, new OrderLineItems(List.of(orderLineItem)));

        // when
        final boolean actual = completeOrder.isNotComplete();

        // then
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("주문 상태를 변경할 때 이미 완료된 주문이면 예외가 발생한다")
    void changeOrderStatus_completeOrder() {
        // given
        final OrderTable orderTable = new OrderTable(4, false);
        final Long menuId = 1L;
        final OrderLineItem orderLineItem = new OrderLineItem(menuId, 2);
        final Order completeOrder = new Order(orderTable.getId(), OrderStatus.COMPLETION, new OrderLineItems(List.of(orderLineItem)));

        // when
        assertThatThrownBy(() -> completeOrder.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문의 상태를 변경할 수 없습니다.");
    }
}
