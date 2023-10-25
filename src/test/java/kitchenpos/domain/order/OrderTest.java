package kitchenpos.domain.order;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    @DisplayName("주문을 생성할 때 테이블이 비어있으면 예외가 발생한다")
    void order_emptyTable() {
        // given
        final OrderTable emptyOrderTable = new OrderTable(4, true);

        // when
        assertThatThrownBy(() -> new Order(emptyOrderTable, OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에는 주문을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 상태를 변경할 때 이미 완료된 주문이면 예외가 발생한다")
    void changeOrderStatus_completeOrder() {
        // given
        final OrderTable orderTable = new OrderTable(4, false);
        final Order completeOrder = new Order(orderTable, OrderStatus.COMPLETION);

        // when
        assertThatThrownBy(() -> completeOrder.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문의 상태를 변경할 수 없습니다.");
    }
}
