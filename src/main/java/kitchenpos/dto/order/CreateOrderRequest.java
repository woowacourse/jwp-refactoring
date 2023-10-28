package kitchenpos.dto.order;

import java.util.List;
import java.util.stream.Collectors;

public class CreateOrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    private CreateOrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public CreateOrderRequest() {
    }

    public static CreateOrderRequest of(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        return new CreateOrderRequest(orderTableId, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public List<Long> getQuantities() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getQuantity)
                .collect(Collectors.toList());
    }
}
