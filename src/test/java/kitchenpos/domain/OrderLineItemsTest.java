package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dto.order.OrderLineItemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @Test
    @DisplayName("from")
    void from() {
        List<OrderLineItemRequest> orderLineItems = new ArrayList<>();

        orderLineItems.add(new OrderLineItemRequest(1L, 1L));
        orderLineItems.add(new OrderLineItemRequest(2L, 3L));

        Order order = new Order(1L, 1L, "COOKING", LocalDateTime.now());

        assertThat(OrderLineItems.from(orderLineItems, order))
            .isInstanceOf(OrderLineItems.class);
    }

    @Test
    @DisplayName("from - order id 가 비어있는 경우 예외처리")
    void from_IfOrderIdIsEmpty_ThrowException() {
        List<OrderLineItemRequest> orderLineItems = new ArrayList<>();

        orderLineItems.add(new OrderLineItemRequest(1L, 1L));
        orderLineItems.add(new OrderLineItemRequest(2L, 3L));

        Order order = new Order(1L, "COOKING", LocalDateTime.now());

        assertThatThrownBy(() -> OrderLineItems.from(orderLineItems, order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("from - 이상한 OrderLineItemRequest 가 포함되어 있는 경우 예외처리")
    void from_IfOrderLineItemRequestsContainsOddThing_ThrowException() {
        List<OrderLineItemRequest> orderLineItems = new ArrayList<>();

        orderLineItems.add(new OrderLineItemRequest(1L, -5L));
        orderLineItems.add(new OrderLineItemRequest(2L, 3L));

        Order order = new Order(1L, 1L, "COOKING", LocalDateTime.now());

        assertThatThrownBy(() -> OrderLineItems.from(orderLineItems, order))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
