package kitchenpos.dto.order;

import kitchenpos.domain.OrderLineItems;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemCreateResponses {
    private List<OrderLineItemCreateResponse> orderLineItemCreateResponses;

    protected OrderLineItemCreateResponses() {
    }

    public OrderLineItemCreateResponses(List<OrderLineItemCreateResponse> orderLineItemCreateResponses) {
        this.orderLineItemCreateResponses = orderLineItemCreateResponses;
    }

    public static OrderLineItemCreateResponses from(OrderLineItems orderLineItems) {
        return orderLineItems.getOrderLineItems().stream()
                .map(OrderLineItemCreateResponse::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), OrderLineItemCreateResponses::new));
    }

    public List<OrderLineItemCreateResponse> getOrderLineItemCreateResponses() {
        return orderLineItemCreateResponses;
    }
}
