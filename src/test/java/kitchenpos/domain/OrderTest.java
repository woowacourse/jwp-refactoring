package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void 주문_상태를_변경한다() {
        // given
        final Order order = new Order();
        order.setOrderStatus(OrderStatus.MEAL);

        // when
        final Order actual = order.updateOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    void 주문_상태를_변경할때_주문_상태가_COMPLETION_이면_예외가_발생한다() {
        // given
        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION);

        // when, then
        assertThatThrownBy(() -> order.updateOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_초기화한다() {
        // given
        final Order order = new Order();
        order.setOrderLineItems(List.of(new OrderLineItem()));
        order.setOrderTableId(1L);
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        // when
        final Order actual = order.init(orderTable, 1L);

        // then
        final Order expected = new Order(null, order.getOrderTableId(), OrderStatus.COOKING, LocalDateTime.now(),
                order.getOrderLineItems());
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("orderedTime")
                .isEqualTo(expected);
    }
}
