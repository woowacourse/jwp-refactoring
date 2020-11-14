package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemCreateRequest;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {
    public static Order createOrder(Long id, OrderStatus status, Long orderTableId, LocalDateTime orderedTime) {
        Order order = createOrder(id, status, orderTableId);
        order.setOrderedTime(orderedTime);
        return order;
    }

    public static Order createOrder(Long id, OrderStatus status, Long orderTableId) {
        Order order = new Order();
        order.setOrderStatus(status.name());
        order.setId(id);
        order.setOrderTableId(orderTableId);
        return order;
    }

    public static OrderCreateRequest createOrderRequest(List<OrderLineItemCreateRequest> orderLineItems, Long orderTableId) {
        return new OrderCreateRequest(orderTableId, orderLineItems);
    }

    public static Order updateOrderRequest(OrderStatus status) {
        Order order = new Order();
        order.setOrderStatus(status.name());
        return order;
    }
}
