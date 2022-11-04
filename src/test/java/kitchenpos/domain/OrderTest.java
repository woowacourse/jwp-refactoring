package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void constructor_주문항목이_빈_값일_경우_예외를_반환한다() {
        assertThatThrownBy(() -> new Order(1L, List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태를 바꾼다.")
    @Test
    void changeStatus() {
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now());
        assertThatThrownBy(() -> order.changeStatus(OrderStatus.COMPLETION.name()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
