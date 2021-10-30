package kitchenpos.integration.templates;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.factory.OrderFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTemplate extends IntegrationTemplate {

    public static final String ORDER_URL = "/api/orders";
    public static final String ORDER_STATUS_URL = ORDER_URL + "/{orderId}/order-status";

    public ResponseEntity<Order> create(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = OrderFactory.builder()
                .orderTableId(orderTableId)
                .orderLineItems(orderLineItems)
                .build();

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
