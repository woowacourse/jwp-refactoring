package kitchenpos.dto.request;

import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, String orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
