package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderLineItems extractOrderLineItems() {
        return new OrderLineItems(orderLineItems.stream()
                .map(orderLineItemRequest -> OrderLineItem.builder()
                        .menuId(orderLineItemRequest.getMenuId())
                        .quantity(orderLineItemRequest.getQuantity())
                        .build())
                .collect(Collectors.toList()));
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
