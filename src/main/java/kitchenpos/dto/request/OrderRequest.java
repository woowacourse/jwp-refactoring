package kitchenpos.dto.request;

import java.util.List;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;
    private String orderStatus;

    private OrderRequest() {
    }

    public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        this.orderStatus = null;
    }

    public OrderRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
        this.orderTableId = null;
        this.orderLineItems = null;
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
