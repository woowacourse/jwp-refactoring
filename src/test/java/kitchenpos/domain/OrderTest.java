package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("상태가 COMPLETION이면, 상태를 변경할 수 없다")
    @Test
    void orderStatusIsCompletion_changeStatus_throwsException() {
        final var order = new Order(1L, 1L, OrderStatus.COMPLETION, LocalDateTime.now(), new ArrayList<>());

        assertThatThrownBy(
                () -> order.changeStatus(OrderStatus.COOKING)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
