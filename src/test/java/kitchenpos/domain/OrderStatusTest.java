package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Test
    void 해당하는_주문_상태가_없으면_예외를_발생시킨다() {
        assertThatThrownBy(() -> OrderStatus.checkIfHas("ㅋㅋ"))
                .isInstanceOf(OrderStatusException.class)
                .hasMessage("해당하는 주문 상태가 없습니다.");
    }
}
