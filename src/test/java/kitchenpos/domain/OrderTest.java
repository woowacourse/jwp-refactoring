package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderTest {

    private final OrderLineItem orderLineItem1 = OrderLineItem.create(1L, 1L);
    private final OrderLineItem orderLineItem2 = OrderLineItem.create(2L, 1L);

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void order() {
        // then
        assertDoesNotThrow(() -> Order.create(1L, List.of(orderLineItem1, orderLineItem2)));
    }

    @DisplayName("주문 상태 변경 시, 현재 주문 상태가 COMPLETION이면 예외가 발생한다.")
    @Test
    void changeOrderStatus_FailWithInvalidOrderStatus() {
        // given
        Order order = Order.create(1L, List.of(orderLineItem1, orderLineItem2));
        order.changeOrderStatus(COMPLETION.name());

        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(COMPLETION.name()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 완료된 주문은 변경할 수 없습니다.");
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus(OrderStatus orderStatus) {
        // given
        Order order = Order.create(1L, List.of(orderLineItem1, orderLineItem2));

        // when
        order.changeOrderStatus(orderStatus.name());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus.name());
    }
}
