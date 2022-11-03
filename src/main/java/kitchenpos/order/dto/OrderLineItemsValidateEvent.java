package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemsValidateEvent {
    private OrderCreateRequest orderCreateRequest;

    private OrderLineItemsValidateEvent() {
    }

    public OrderLineItemsValidateEvent(final OrderCreateRequest orderCreateRequest) {
        this.orderCreateRequest = orderCreateRequest;
    }

    public OrderCreateRequest getOrderCreateRequest() {
        return orderCreateRequest;
    }

    public List<Long> getOrderLineItemsMenuIds() {
        return orderCreateRequest.getOrderLineItems()
                .stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
    }
}
