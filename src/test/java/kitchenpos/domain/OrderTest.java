package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {
    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void constructor() {
        Order order = new Order(1L, 1L, OrderStatus.COOKING, LocalDateTime.MIN);

        assertAll(
            () -> assertThat(order.getId()).isEqualTo(1L),
            () -> assertThat(order.getOrderTableId()).isEqualTo(1L),
            () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
            () -> assertThat(order.getOrderedTime()).isEqualTo(LocalDateTime.MIN)
        );
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        Order order = new Order(1L, 1L, OrderStatus.COOKING, LocalDateTime.MIN);

        order.changeOrderStatus(OrderStatus.MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("완료된 주문의 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus_throws_exception() {
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION, LocalDateTime.MIN);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL));
    }
}
