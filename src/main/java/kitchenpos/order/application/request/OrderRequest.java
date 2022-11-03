package kitchenpos.order.application.request;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.OrderLineItem;

public class OrderRequest {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItemRequests;

    private OrderRequest() {
    }

    public OrderRequest(Long id, Long orderTableId, String orderStatus,
        List<OrderLineItemRequest> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemRequests = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public List<OrderLineItem> toOrderLineItems() {
        return orderLineItemRequests.stream()
            .map(OrderLineItemRequest::toEntity)
            .collect(Collectors.toUnmodifiableList());
    }
}
