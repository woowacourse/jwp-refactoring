package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 주문_생성() {
        Assertions.assertDoesNotThrow(() -> new Order(1L, OrderStatus.COOKING, List.of(new OrderLineItem(1L, 1L, 1))));
    }

    @Test
    void 주문_정보가_없는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> new Order(1L, OrderStatus.COOKING, null));
    }

    @Test
    void 주문의_상태를_확인한다() {
        Order order = new Order(1L, OrderStatus.COMPLETION, List.of(new OrderLineItem(1L, 1L, 1)));
        assertThat(order.isNotChangeStatus()).isTrue();
    }
}
