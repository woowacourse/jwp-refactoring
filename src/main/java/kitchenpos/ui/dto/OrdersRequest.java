package kitchenpos.ui.dto;

import kitchenpos.domain.OrderStatus;

import java.util.List;

public class OrdersRequest {

    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItemRequestList;

    public OrdersRequest() {
    }

    public OrdersRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItemRequestList) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemRequestList = orderLineItemRequestList;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequestList() {
        return orderLineItemRequestList;
    }
}
