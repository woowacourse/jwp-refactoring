package kitchenpos.ui.dto.order;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;

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
                new OrderTable(orderTableId),
                null,
                null,
                orderLineItems.stream()
                        .map(OrderLineItemRequest::toEntity)
                        .collect(Collectors.toList())
        );
    }
}
