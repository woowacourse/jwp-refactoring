package kitchenpos.application.dto;

import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long orderTableId, final String orderStatus,
                              final List<OrderLineItemCreateRequest> orderLineItems) {
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

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
