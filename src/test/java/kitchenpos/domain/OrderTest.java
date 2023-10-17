package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void 같은_상태로_변경하면_예외가_발생한다() {
        //given
        Order 주문 = new Order(1L, 1L, OrderStatus.COOKING.name(), null, null);

        //expect
        assertThatThrownBy(() -> 주문.setOrderStatus(OrderStatus.COOKING.name()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("같은 상태로 변경할 수 없습니다.");
    }

}
