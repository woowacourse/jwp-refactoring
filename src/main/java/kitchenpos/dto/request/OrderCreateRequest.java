package kitchenpos.dto.request;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderCreateRequest {
    private final Long orderTableId;
    private final List<OrderLineItemCreateRequest> orderLineItems;

    @ConstructorProperties({"orderTableId", "orderLineItems"})
    public OrderCreateRequest(Long orderTableId, List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity() {
        return Order.of(orderTableId, OrderStatus.COOKING);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
            .map(OrderLineItemCreateRequest::getMenuId)
            .collect(Collectors.toList());
    }
}
