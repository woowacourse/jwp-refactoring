package kitchenpos.dto;

import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;
    private String orderStatus;

    public OrderRequest(String orderStatus) {
        this(null, null, orderStatus);
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this(orderTableId, orderLineItemRequests, null);
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests, String orderStatus) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
