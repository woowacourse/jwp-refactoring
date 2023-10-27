package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import kitchenpos.common.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        final OrderTable orderTable = new OrderTable(null, 2, false);
        final Order order = new Order(new ArrayList<>(), orderTable.getId(), OrderStatus.COOKING);

        // when
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    @DisplayName("주문 상태를 변경할 때 주문의 상태가 결제완료라면 예외가 발생한다.")
    void throwsExceptionWhenOrderStatusIsAlreadyCompletion() {
        // given
        final OrderTable orderTable = new OrderTable(null, 2, false);
        final Order order = new Order(new ArrayList<>(), orderTable.getId(), OrderStatus.COMPLETION);

        // when
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제완료 상태인 경우 주문상태를 변경할 수 없습니다.");
    }
}
