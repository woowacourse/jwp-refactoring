package kitchenpos.ui.dto;

import kitchenpos.domain.Order;

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

    public Order toEntity() {
        return new Order(orderTableId, this.orderLineItems.stream()
                .map(OrderLineItemCreateRequest::toEntity)
                .collect(Collectors.toList()));
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public int countOrderLineItems() {
        return orderLineItems.size();
    }
}
