package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Order 는 ")
class OrderTest {

    @DisplayName("주문 상태가 완료인지를 판단한다.")
    @Test
    void isInCompletionStatus() {
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 1L));
        final Order order = new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now(), orderLineItems);

        assertThat(order.isInCompletionStatus()).isTrue();
    }
}
