package kitchenpos.order.ui.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;

import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Order toEntity() {
        return new Order(
                orderTableId,
                null,
                null,
                new OrderLineItems(this.orderLineItems.stream()
                        .map(OrderLineItemRequest::toEntity)
                        .collect(Collectors.toList()))
        );
    }
}
