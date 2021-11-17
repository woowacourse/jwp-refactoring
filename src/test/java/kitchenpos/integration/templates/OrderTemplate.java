package kitchenpos.integration.templates;

import java.util.List;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.factory.OrderLineItemFactory;
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

    public ResponseEntity<OrderResponse> create(Long orderTableId, List<OrderLineItem> orderLineItems) {
        OrderRequest orderRequest = new OrderRequest(
            null,
            orderTableId,
            null,
            null,
            OrderLineItemFactory.dtoList(orderLineItems)
        );

        return integrationTemplate.post(
            ORDER_URL,
            orderRequest,
            OrderResponse.class
        );
    }

    public ResponseEntity<OrderResponse[]> list() {
        return integrationTemplate.get(
            ORDER_URL,
            OrderResponse[].class
        );
    }

    public ResponseEntity<OrderResponse> changeOrderStatus(Long orderId, OrderRequest orderRequest) {
        return integrationTemplate.put(
            ORDER_STATUS_URL,
            orderId,
            orderRequest,
            OrderResponse.class
        );
    }
}
