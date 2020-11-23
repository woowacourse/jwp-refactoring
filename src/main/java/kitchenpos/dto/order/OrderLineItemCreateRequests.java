package kitchenpos.dto.order;

import kitchenpos.domain.OrderLineItems;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemCreateRequests {
    private List<OrderLineItemCreateRequest> orderLineItemCreateRequest;

    protected OrderLineItemCreateRequests() {
    }

    public OrderLineItemCreateRequests(List<OrderLineItemCreateRequest> orderLineItemCreateRequest) {
        this.orderLineItemCreateRequest = orderLineItemCreateRequest;
    }

    public static OrderLineItemCreateRequests from(OrderLineItems orderLineItems) {
        return orderLineItems.getOrderLineItems().stream()
                .map(OrderLineItemCreateRequest::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), OrderLineItemCreateRequests::new));
    }

    public OrderLineItems toOrderLineItems() {
        return orderLineItemCreateRequest.stream()
                .map(OrderLineItemCreateRequest::toEntity)
                .collect(Collectors.collectingAndThen(Collectors.toList(), OrderLineItems::new));
    }

    public List<OrderLineItemCreateRequest> getOrderLineItemResponse() {
        return orderLineItemCreateRequest;
    }
}
