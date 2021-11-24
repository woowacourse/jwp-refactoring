package kitchenpos.ui.dto.request;

import java.util.List;

public class OrdersRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    private OrdersRequest() {
    }

    public OrdersRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
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
