package kitchenpos.dto;

import java.util.List;

public class OrderSaveRequest {

    private Long orderTableId;
    private List<OrderLineItemSaveRequest> orderLineItems;

    private OrderSaveRequest() {
    }

    public OrderSaveRequest(final Long orderTableId, final List<OrderLineItemSaveRequest> orderLineItems) {
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
