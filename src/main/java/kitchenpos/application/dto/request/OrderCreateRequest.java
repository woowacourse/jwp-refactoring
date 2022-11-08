package kitchenpos.application.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemCreateRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<OrderLineItem> toOrderLineItems() {
        return orderLineItems.stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
    }
}
