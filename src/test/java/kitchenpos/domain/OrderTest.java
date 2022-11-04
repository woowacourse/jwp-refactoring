package kitchenpos.domain;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    @DisplayName("주문 테이블 id가 null인 주문을 생성할 수 없다")
    @Test
    void create_tableIdNull() {
        assertThatThrownBy(() -> new Order(null, OrderStatus.COOKING, LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("완료된 주문의 상태를 바꿀 수 없다")
    @Test
    void changeOrderStatus_statusCompletion() {
        Order order = new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now());

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
