package kitchenpos.order;

import static kitchenpos.order.OrderStatus.COMPLETION;
import static kitchenpos.order.OrderStatus.COOKING;
import static kitchenpos.order.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import kitchenpos.ordertable.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private final OrderTable orderTable = OrderTable.ofUnsaved(2, false);

    @DisplayName("테이블에 대해 주문을 생성하면 주문의 상태는 조리중이다.")
    @Test
    void ofUnsaved() {
        final Order order = Order.ofUnsaved(orderTable);

        assertThat(order.getOrderStatus()).isEqualTo(COOKING.name());
    }

    @DisplayName("빈 테이블에 대해 주문을 생성하면 예외가 발생한다.")
    @Test
    void ofUnsaved_throwsException_ifTableEmpty() {
        final OrderTable emptyTable = OrderTable.ofUnsaved(0, true);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Order.ofUnsaved(emptyTable));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeStatus() {
        final Order order = Order.ofUnsaved(orderTable);
        order.changeStatus(MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @DisplayName("완료된 주문의 상태를 변경할 수 없다.")
    @Test
    void changeStatus_throwsException_ifCompleted() {
        final Order completedOrder = new Order(null, null, COMPLETION, null);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> completedOrder.changeStatus(MEAL));
    }
}
