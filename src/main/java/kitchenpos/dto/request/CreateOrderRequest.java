package kitchenpos.dto.request;

import java.util.List;

public class CreateOrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
