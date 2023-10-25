package kitchenpos.ui.dto;

import java.util.List;

public class CreateOrderRequest {

    private Long orderTableId;
    private List<CreateOrderLineItemRequest> orderLineItems;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(final Long orderTableId, final List<CreateOrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<CreateOrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
