package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문을 생성할 떄 생성일자가 빈 값이면 예외가 발생한다.")
    @Test
    void createFailureWhenTimeIsNull() {
        assertThatThrownBy(
                () -> new Order(1L, OrderStatus.COOKING, null))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 주문 시간이나 주문 상태는 빈 값이면 안됩니다.");
    }

    @DisplayName("주문을 생성할 떄 주문 상태가 빈 값이면 예외가 발생한다.")
    @Test
    void createFailureWhenStatusIsNull() {
        assertThatThrownBy(
                () -> new Order(1L, null, LocalDateTime.now()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 주문 시간이나 주문 상태는 빈 값이면 안됩니다.");
    }

    @DisplayName("추가하려는 주문 항목이 빈값이면 예외가 발생하다.")
    @Test
    void addOrderLineItems() {
        Order order = Order.create(1L);
        assertThatThrownBy(
                () -> order.addOrderLineItems(Collections.emptyList()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 주문 항목들이 빈 값이면 안됩니다.");
    }

    @DisplayName("주문을 상태가 COMPLETION이면 true를 반환한다.")
    @Test
    void isCompletionStatus() {
        Order order = new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now());

        assertThat(order.isCompletionStatus()).isTrue();
    }
}
