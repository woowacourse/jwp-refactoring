package kitchenpos.integration.templates;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.factory.OrderFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderTemplate {

    public static final String ORDER_URL = "/api/orders";
    public static final String ORDER_STATUS_URL = ORDER_URL + "/{orderId}/order-status";

    private final IntegrationTemplate integrationTemplate;

    public OrderTemplate(IntegrationTemplate integrationTemplate) {
        this.integrationTemplate = integrationTemplate;
    }

    public ResponseEntity<Order> create(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = OrderFactory.builder()
            .orderTableId(orderTableId)
            .orderLineItems(orderLineItems)
            .build();

        return integrationTemplate.post(
            ORDER_URL,
            order,
            Order.class
        );
    }

    public ResponseEntity<Order[]> list() {
        return integrationTemplate.get(
            ORDER_URL,
            Order[].class
        );
    }

    public ResponseEntity<Order> changeOrderStatus(Long orderId, Order order) {
        return integrationTemplate.put(
            ORDER_STATUS_URL,
            orderId,
            order,
            Order.class
        );
    }
}
