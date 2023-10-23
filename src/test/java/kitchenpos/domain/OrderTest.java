package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.exception.InvalidOrderException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OrderTest {
    @Nested
    class 주문_생성시 {
        @Test
        void 주문_테이블_아이디가_null이면_예외가_발생한다() {
            List<OrderLineItem> orderLineItems = List.of((new OrderLineItem(1L, 1L, 1)));
            assertThatThrownBy(() -> new Order(null, OrderStatus.MEAL.name(), orderLineItems))
                    .isInstanceOf(InvalidOrderException.class);
        }

        @Test
        void 주문_항목_리스트가_비어있으면_예외가_발생한다() {
            assertThatThrownBy(() -> new Order(1L, OrderStatus.MEAL.name(), Collections.emptyList()))
                    .isInstanceOf(InvalidOrderException.class);
        }
    }
}
