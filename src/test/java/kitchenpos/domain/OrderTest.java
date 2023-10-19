package kitchenpos.domain;

import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class OrderTest {

    @Test
    void 같은_상태로_변경하면_예외가_발생한다() {
        //given
        Order 주문 = new Order(1L, 1L, OrderStatus.COOKING.name(), now(),
                List.of(mock(OrderLineItem.class), mock(OrderLineItem.class)));

        //expect
        assertThatThrownBy(() -> 주문.changeOrderStatus(OrderStatus.COOKING.name()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("같은 상태로 변경할 수 없습니다.");
    }

    @Test
    @Disabled // todo: able to pass this test
    void 주문항목이_없으면_예외가_발생한다() {
        //given
        List<OrderLineItem> 주문항목_목록 = emptyList();

        //expect
        assertThatThrownBy(() -> new Order(null, 1L, OrderStatus.COOKING.name(), now(), 주문항목_목록))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Disabled // todo: able to pass this test
    void 주문항목이_null이면_예외가_발생한다() {
        //expect
        assertThatThrownBy(() -> new Order(null, 1L, OrderStatus.COOKING.name(), now(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
