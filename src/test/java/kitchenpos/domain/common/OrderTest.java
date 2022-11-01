package kitchenpos.domain.common;

import static kitchenpos.domain.common.OrderStatus.COMPLETION;
import static kitchenpos.domain.common.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.exception.badrequest.CompletedOrderCannotChangeException;
import kitchenpos.exception.badrequest.OrderLineItemNotExistsException;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 주문_항목이_하나_이상_있어야_한다() {
        assertThatThrownBy(() -> new Order(1L, List.of()))
                .isInstanceOf(OrderLineItemNotExistsException.class);
    }

    @Test
    void 주문_상태가_완료인_주문의_상태는_변경할_수_없다() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1);
        Order order = new Order(1L, COMPLETION, List.of(orderLineItem));
        assertThatThrownBy(() -> order.changeOrderStatus(MEAL.name()))
                .isInstanceOf(CompletedOrderCannotChangeException.class);
    }
}
