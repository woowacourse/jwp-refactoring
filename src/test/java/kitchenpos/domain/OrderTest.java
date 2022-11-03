package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("ALL")
class OrderTest {

    @Test
    void 주문_생성() {
        Assertions.assertDoesNotThrow(() -> Order.of(1L, OrderStatus.COOKING, List.of(new OrderLineItem(1L, 1L, 1))));
    }

    @Test
    void 주문_정보가_없는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> Order.of(1L, OrderStatus.COOKING, null));
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 주문의_상태를_변경한다(OrderStatus orderStatus) {
        Order order = Order.of(1L, orderStatus, List.of(new OrderLineItem(1L, 1L, 1)));

        Assertions.assertDoesNotThrow(() -> order.changeOrderStatus(OrderStatus.COMPLETION));
    }

    @Test
    void 주문의_상태가_완료된_경우_상태를_변경할_수_없다() {
        Order order = Order.of(1L, OrderStatus.COMPLETION, List.of(new OrderLineItem(1L, 1L, 1)));
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COMPLETION))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 완료된 상태이므로 상태를 변화시킬 수 없습니다.");
    }
}
