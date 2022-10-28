package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 주문_항목이_하나_이상_있어야_한다() {
        assertThatThrownBy(() -> new Order(null, 1L, COOKING.name(), LocalDateTime.now(), List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태가_완료인_주문의_상태는_변경할_수_없다() {
        OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1);
        Order order = new Order(null, 1L, COMPLETION.name(), LocalDateTime.now(), List.of(orderLineItem));
        assertThatThrownBy(() -> order.changeOrderStatus(MEAL.name())).isInstanceOf(IllegalArgumentException.class);
    }
}
