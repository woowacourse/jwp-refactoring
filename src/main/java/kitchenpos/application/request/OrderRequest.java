package kitchenpos.application.request;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderLineItem;

public class OrderRequest {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    private OrderRequest() {
    }

    public OrderRequest(Long id, Long orderTableId, String orderStatus,
        List<OrderLineItemRequest> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest(final String orderStatus) {
        this(null, null, orderStatus, null);
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.stream()
            .map(OrderLineItemRequest::toEntity)
            .collect(Collectors.toUnmodifiableList());
    }
}
