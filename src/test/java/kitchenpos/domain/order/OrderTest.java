package kitchenpos.domain.order;

import static kitchenpos.domain.order.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class OrderTest {

    @Test
    void 주문_상품_등록_시_주문_상품이_빈값이면_예외가_발생한다() {
        final Order order = new Order(1L, COOKING);

        assertThatThrownBy(() -> order.updateOrderLineItems(new OrderLineItems(new ArrayList<>())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상품이 존재하지 않습니다.");
    }

    @Test
    void 이미_완료된_주문은_상태를_변경할_수_없다() {
        final Order order = new Order(1L, OrderStatus.COMPLETION);

        assertThatThrownBy(() -> order.changeStatus(COOKING))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 완료된 Order의 상태는 변경할 수 없습니다.");
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 완료되지_않은_주문은_상태를_변경할_수_있다(final OrderStatus orderStatus) {
        final Order order = new Order(1L, orderStatus);

        order.changeStatus(COOKING);

        assertThat(order.getOrderStatus()).isEqualTo(COOKING);
    }
}
