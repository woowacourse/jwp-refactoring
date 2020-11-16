package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderCreateRequest {
    private final Long orderTableId;
    private final List<OrderLineItemCreateRequest> orderLineItems;

    @JsonCreator
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
