package kitchenpos.dto;

import java.util.List;

public class OrderCreateRequest {
    private Long orderTableId;
    private List<OrderLineItemCreateInfo> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemCreateInfo> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateInfo> getOrderLineItems() {
        return orderLineItems;
    }
}
