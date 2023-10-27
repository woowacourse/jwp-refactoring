package order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import common.domain.Price;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        final Order order = new Order(1L, OrderStatus.COOKING,
                                      List.of(new OrderLineItem(1L, 1L, "피자", new Price(BigDecimal.TEN), null)), 1L);

        // when
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("완료된 주문 상태를 변경하려고 시도하면 예외가 발생한다.")
    @Test
    void changeOrderStatus_failAlreadyCompletion() {
        // given
        final Order order = new Order(1L, OrderStatus.COMPLETION,
                                      List.of(new OrderLineItem(1L, 1L, "피자", new Price(BigDecimal.TEN), null)), 1L);

        // when
        // then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COMPLETION))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
