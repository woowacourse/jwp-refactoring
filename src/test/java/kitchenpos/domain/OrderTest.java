package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    @DisplayName("주문 항목이 비어있으면 예외를 던진다.")
    void orderLineItems_empty_throwException() {
        // when & then
        assertThatThrownBy(() -> new Order(1L, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목의 메뉴 아이디를 반환한다.")
    void getOrderMenuIds() {
        // given
        final Order order = new Order(1L,
                List.of(new OrderLineItem(1L, 3),
                        new OrderLineItem(2L, 5)));

        // when
        final List<Long> menuIds = order.getMenuIds();

        // then
        assertThat(menuIds).isEqualTo(List.of(1L, 2L));
    }

    @Test
    @DisplayName("주문 항목의 사이즈를 반환한다.")
    void getItemSize() {
        // given
        final Order order = new Order(1L,
                List.of(new OrderLineItem(1L, 3),
                        new OrderLineItem(2L, 5)));

        // when
        final int itemSize = order.getItemSize();

        // then
        assertThat(itemSize).isEqualTo(2);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        final Order order = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(),
                List.of(new OrderLineItem(1L, 3),
                        new OrderLineItem(2L, 5)));

        // when
        order.changeOrderStatus(OrderStatus.MEAL.name());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("이미 계산 완료된 주문의 상태를 변경할 경우 예외를 던진다.")
    void changeOrderStatus_alreadyCompletion_throwException() {
        // given
        final Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                List.of(new OrderLineItem(1L, 3),
                        new OrderLineItem(2L, 5)));

        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL.name()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
