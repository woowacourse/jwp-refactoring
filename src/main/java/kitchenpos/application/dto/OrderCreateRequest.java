package kitchenpos.application.dto;

import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private String orderStatus;
    private List<Long> orderLineItemIds;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long orderTableId, final String orderStatus, final List<Long> orderLineItemIds) {
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
