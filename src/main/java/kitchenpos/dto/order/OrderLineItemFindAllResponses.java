package kitchenpos.dto.order;

import kitchenpos.domain.OrderLineItems;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemFindAllResponses {
    private List<OrderLineItemFindAllResponse> orderLineItemFindAllResponses;

    protected OrderLineItemFindAllResponses() {
    }

    public OrderLineItemFindAllResponses(List<OrderLineItemFindAllResponse> orderLineItemFindAllResponses) {
        this.orderLineItemFindAllResponses = orderLineItemFindAllResponses;
    }

    public static OrderLineItemFindAllResponses from(OrderLineItems orderLineItems) {
        return orderLineItems.getOrderLineItems().stream()
                .map(OrderLineItemFindAllResponse::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), OrderLineItemFindAllResponses::new));
    }

    public List<OrderLineItemFindAllResponse> getOrderLineItemFindAllResponses() {
        return orderLineItemFindAllResponses;
    }
}
