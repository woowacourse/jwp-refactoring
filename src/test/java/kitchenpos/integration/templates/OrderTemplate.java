package kitchenpos.integration.templates;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTemplate extends IntegrationTemplate {

    public static final String ORDER_URL = "/api/orders";
    public static final String ORDER_STATUS_URL = ORDER_URL + "/{orderId}/order-status";

    public ResponseEntity<Order> create(List<OrderLineItem> orderLineItems, Long orderTableId) {
        Order order = new Order();
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTableId);

        return post(
                ORDER_URL,
                order,
                Order.class
        );
    }

    public ResponseEntity<Order[]> list() {
        return get(
                ORDER_URL,
                Order[].class
        );
    }

    public ResponseEntity<Order> changeOrderStatus(Long orderId, Order order) {
        return put(
                ORDER_STATUS_URL,
                orderId,
                order,
                Order.class
        );
    }
}
