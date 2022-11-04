package kitchenpos.domain.order;

import static kitchenpos.domain.common.OrderStatus.COMPLETION;
import static kitchenpos.domain.common.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.common.Price;
import kitchenpos.exception.badrequest.CompletedOrderCannotChangeException;
import kitchenpos.exception.badrequest.OrderLineItemNotExistsException;
import kitchenpos.exception.badrequest.OrderTableEmptyException;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 빈_테이블에는_주문을_할_수_없다() {
        assertThatThrownBy(() -> new Order(1L, List.of(), true))
                .isInstanceOf(OrderTableEmptyException.class);
    }

    @Test
    void 주문_항목이_하나_이상_있어야_한다() {
        assertThatThrownBy(() -> new Order(1L, List.of(), false))
                .isInstanceOf(OrderLineItemNotExistsException.class);
    }

    @Test
    void 주문_상태가_완료인_주문의_상태는_변경할_수_없다() {
        OrderLineItem orderLineItem = new OrderLineItem(1, new OrderedMenu("메뉴 이름", new Price(BigDecimal.ZERO)));
        Order order = new Order(1L, List.of(orderLineItem), false);
        order.changeOrderStatus(COMPLETION.name());
        assertThatThrownBy(() -> order.changeOrderStatus(MEAL.name()))
                .isInstanceOf(CompletedOrderCannotChangeException.class);
    }
}
