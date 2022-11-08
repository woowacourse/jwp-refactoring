package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class OrderTest {

    @Test
    void 이미_완료된_주문은_상태를_변경할_수_없다() {
        final Order order = new Order(1L, OrderStatus.COMPLETION, List.of(new OrderLineItem(1L, 1L, null)));

        assertThatThrownBy(() -> order.changeStatus(COOKING))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 완료된 Order의 상태는 변경할 수 없습니다.");
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 완료되지_않은_주문은_상태를_변경할_수_있다(final OrderStatus orderStatus) {
        final Order order = new Order(1L, orderStatus, List.of(new OrderLineItem(1L, 1L, null)));

        order.changeStatus(COOKING);

        assertThat(order.getOrderStatus()).isEqualTo(COOKING);
    }
}
