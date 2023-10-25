package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemCreateRequest> orderLineItems = new ArrayList<>();

    protected OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.stream()
                .map(request -> new OrderLineItem(request.getMenuId(), request.getQuantity()))
                .collect(Collectors.toList());
    }
}
