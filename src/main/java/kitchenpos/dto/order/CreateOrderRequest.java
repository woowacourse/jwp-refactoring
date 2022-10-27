package kitchenpos.dto.order;

import java.util.List;

public class CreateOrderRequest {

    private Long orderTableId;
    private List<CreateOrderLineItemRequest> orderLineItems;

    public CreateOrderRequest(Long orderTableId, List<CreateOrderLineItemRequest> orderLineItems) {
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
