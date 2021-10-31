package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.OrderLineItem;

public class OrderRequest {

    private Long orderTableId;
    private String orderStatus;
    private List<Long> orderLineItemIds;

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
