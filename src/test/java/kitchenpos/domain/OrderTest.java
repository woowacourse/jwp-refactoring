package kitchenpos.domain;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import org.junit.jupiter.api.Test;

import static java.time.LocalDateTime.now;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class OrderTest {

    @Test
    void 같은_상태로_변경하면_예외가_발생한다() {
        //given
        Order 주문 = new Order(1L, 1L, COOKING, now(),
                List.of(mock(OrderLineItem.class), mock(OrderLineItem.class)));

        //expect
        assertThatThrownBy(() -> 주문.changeOrderStatus(COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("같은 상태로 변경할 수 없습니다.");
    }

}
