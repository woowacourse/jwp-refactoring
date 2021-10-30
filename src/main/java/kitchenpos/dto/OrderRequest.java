package kitchenpos.dto;

import java.util.List;

public class OrderRequest {

    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest() {
    }

    public OrderRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public void setOrderLineItemRequests(final List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderLineItemRequests = orderLineItemRequests;
    }
}
