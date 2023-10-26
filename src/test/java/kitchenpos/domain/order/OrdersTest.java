package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrdersTest {

    @Test
    @DisplayName("완료되지 않은 주문이 1개라도 있으면 true를 반환한다")
    void containsNotCompleteOrder_true() {
        // given
        final OrderTable orderTable = new OrderTable(4, false);

        final OrderStatus notComplete = OrderStatus.COOKING;
        final Order notCompleteOrder = new Order(orderTable.getId(), notComplete);
        final Order completeOrder1 = new Order(orderTable.getId(), OrderStatus.COMPLETION);
        final Order completeOrder2 = new Order(orderTable.getId(), OrderStatus.COMPLETION);

        final Orders orders = new Orders(List.of(notCompleteOrder, completeOrder1, completeOrder2));

        // when
        final boolean actual = orders.containsNotCompleteOrder();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("모두 다 완료된 주문일 때만 false를 반환한다")
    void containsNotCompleteOrder_false() {
        // given
        final OrderTable orderTable = new OrderTable(4, false);

        final Order completeOrder1 = new Order(orderTable.getId(), OrderStatus.COMPLETION);
        final Order completeOrder2 = new Order(orderTable.getId(), OrderStatus.COMPLETION);
        final Order completeOrder3 = new Order(orderTable.getId(), OrderStatus.COMPLETION);

        final Orders orders = new Orders(List.of(completeOrder1, completeOrder2, completeOrder3));

        // when
        final boolean actual = orders.containsNotCompleteOrder();

        // then
        assertThat(actual).isFalse();
    }
}
