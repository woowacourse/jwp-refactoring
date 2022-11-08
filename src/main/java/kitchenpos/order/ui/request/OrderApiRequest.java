package kitchenpos.order.ui.request;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.application.request.OrderLineItemRequest;
import kitchenpos.order.application.request.OrderRequest;

public class OrderApiRequest {

    private final Long orderTableId;
    private final List<OrderLineItemApiRequest> orderLineItems;

    public OrderApiRequest(Long orderTableId, List<OrderLineItemApiRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest toServiceRequest() {
        List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                .map(OrderLineItemApiRequest::toServiceRequest)
                .collect(Collectors.toList());

        return new OrderRequest(orderTableId, orderLineItemRequests);
    }
}
