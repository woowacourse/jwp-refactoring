package kitchenpos.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;

public class OrderRequest {

    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::toEntity)
                .collect(Collectors.toList());
    }
}
