package kitchenpos.ui.dto;

import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemsOfOrderRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId,
        List<OrderLineItemsOfOrderRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemsOfOrderRequest> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public String toString() {
        return "OrderCreateRequest{" +
            "orderTableId=" + orderTableId +
            ", orderLineItems=" + orderLineItems +
            '}';
    }
}
