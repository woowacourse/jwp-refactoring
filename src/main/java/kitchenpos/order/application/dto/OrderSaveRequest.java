package kitchenpos.order.application.dto;

import java.util.List;

public class OrderSaveRequest {

    private final Long orderTableId;
    private final List<OrderLineItemSaveRequest> orderLineItems;

    public OrderSaveRequest(Long orderTableId, List<OrderLineItemSaveRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemSaveRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
