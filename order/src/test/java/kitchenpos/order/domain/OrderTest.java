package kitchenpos.order.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.Collections;
import java.util.List;

import kitchenpos.order.exception.InvalidOrderException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OrderTest {
    @Nested
    class 주문_생성시 {
        @Test
        void 주문_항목_리스트가_비어있으면_예외가_발생한다() {
            assertThatThrownBy(() -> new Order(1L, OrderStatus.MEAL.name(), Collections.emptyList()))
                    .isInstanceOf(InvalidOrderException.class);
        }
    }

    @Test
    void 주문_상태가_계산_완료일_때_주문_상태를_변경하면_예외가_발생한다() {
        final List<OrderLineItem> orderLineItems = List.of((new OrderLineItem(1L, 1L, 1)));
        final Order order = new Order(1L, OrderStatus.COMPLETION.name(), orderLineItems);
        assertThatThrownBy(() -> order.changeStatus(OrderStatus.MEAL))
                .isInstanceOf(InvalidOrderException.class);
    }
}
