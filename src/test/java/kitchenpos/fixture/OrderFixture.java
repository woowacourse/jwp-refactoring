package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemCreateRequest;
import kitchenpos.ui.dto.OrderUpdateRequest;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {
    public static Order createOrder(Long id, OrderStatus status, Long orderTableId, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, status.name(), orderedTime, orderLineItems);
    }

    public static Order createOrder(Long id, OrderStatus status, Long orderTableId, LocalDateTime orderedTime) {
        return new Order(id, orderTableId, status.name(), orderedTime);
    }

    public static Order createOrder(Long id, OrderStatus status, Long orderTableId) {
        return createOrder(id, status, orderTableId, LocalDateTime.now());
    }

    public static OrderCreateRequest createOrderRequest(List<OrderLineItemCreateRequest> orderLineItems, Long orderTableId) {
        return new OrderCreateRequest(orderTableId, orderLineItems);
    }

    public static OrderUpdateRequest updateOrderRequest(OrderStatus status) {
        return new OrderUpdateRequest(status);
    }
}
