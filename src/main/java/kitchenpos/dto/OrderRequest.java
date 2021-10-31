package kitchenpos.dto;

import java.util.List;

public class OrderRequest {

    private final Long orderTableId;
    private final String orderStatus;
    private final List<Long> orderLineItemIds;

    public OrderRequest(Long orderTableId, String orderStatus,
        List<Long> orderLineItemIds) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemIds = orderLineItemIds;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<Long> getOrderLineItemIds() {
        return orderLineItemIds;
    }
}
