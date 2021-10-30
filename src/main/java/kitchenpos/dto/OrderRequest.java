package kitchenpos.dto;

import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;
    private String orderStatus;

    public OrderRequest() {
    }

    public OrderRequest(String orderStatus) {
        this(null, null, orderStatus);
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this(orderTableId, orderLineItems, null);
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems, String orderStatus) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
