package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.table.OrderTable;

class OrderTest {

    @DisplayName("비어있는 테이블에서는 주문을 할 수 없다")
    @Test
    void validateOrderTableIsNotEmpty() {
        OrderTable orderTable = new OrderTable(5, true);

        assertThatThrownBy(() -> new Order(orderTable, OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블이 비어있습니다.");
    }

    @DisplayName("상태를 올바르게 변경한다")
    @Test
    void changeStatus() {
        OrderStatus orderStatus = OrderStatus.MEAL;
        Order order = new Order(new OrderTable(10, false), orderStatus);
        OrderStatus toChangeStatus = OrderStatus.COMPLETION;
        order.changeStatus(toChangeStatus);

        OrderStatus actual = order.getOrderStatus();
        assertThat(actual).isEqualTo(toChangeStatus);
    }

    @DisplayName("Complete상태에서 상태 변경시 예외가 발생한다")
    @Test
    void changeStatusWithCompleteStatus() {
        OrderStatus orderStatus = OrderStatus.COMPLETION;
        Order order = new Order(new OrderTable(10, false), orderStatus);

        OrderStatus toChangeStatus = OrderStatus.MEAL;
        assertThatThrownBy(() -> order.changeStatus(toChangeStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
