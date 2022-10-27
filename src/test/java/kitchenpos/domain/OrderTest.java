package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    @DisplayName("주문 테이블이 비어있을 수 없다")
    void emptyTable() {
        // given
        OrderTable orderTable = new OrderTable(100, true);

        // when, then
        assertThatThrownBy(() -> new Order(orderTable, List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void updateStatus() {
        // given
        OrderTable orderTable = new OrderTable(100, false);
        Order order = new Order(orderTable, List.of());

        // when
        order.updateStatus(OrderStatus.MEAL);

        // when, then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    @DisplayName("완료된 주문의 상태를 변경할 수 없다")
    void setOrderTableId() {
        // given
        OrderTable orderTable = new OrderTable(100, false);
        Order order = new Order(orderTable, List.of());

        // when
        order.updateStatus(OrderStatus.COMPLETION);

        // then
        assertThatThrownBy(() -> order.updateStatus(OrderStatus.MEAL))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
