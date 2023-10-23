package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 빈_테이블인_경우_예외가_발생한다() {
        // given
        OrderTable orderTable = new OrderTable(true);

        // when, then
        assertThatThrownBy(() -> new Order(orderTable, COOKING, LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문항목이_비어있는_경우_예외가_발생한다() {
        // given
        OrderTable orderTable = new OrderTable(false);
        Order order = new Order(orderTable, COOKING, LocalDateTime.now());

        // when, then
        assertThatThrownBy(() -> order.setOrderLineItems(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
