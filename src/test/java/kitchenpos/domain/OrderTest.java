package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문 항목이 없는 경우 예외 발생")
    void validateOrderLineItems() {
        assertThatThrownBy(() -> Order.of(1L, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목의 크기와 메뉴의 크기가 맞지 않으면 예외 발생")
    void checkMenuSize() {
        final Order order = Order.of(1L, Collections.singletonList(new OrderLineItem(1L, 1L)));

        assertThatThrownBy(() -> order.checkActualOrderLineItems(3L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴들의 id 목록을 가져온다")
    void getMenuIds() {
        final Order order = Order.of(1L, List.of(new OrderLineItem(1L, 1L), new OrderLineItem(2L, 3L)));

        assertThat(order.getMenuIds())
                .containsExactly(1L, 2L);
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        final Order order = Order.of(1L, Collections.singletonList(new OrderLineItem(1L, 1L)));

        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    @DisplayName("주문의 상태가 완료 상태일때 상태를 변경하면 예외 발생")
    void whenOrderStatusIsCompletion() {
        final Order order = Order.of(1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L)));

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
