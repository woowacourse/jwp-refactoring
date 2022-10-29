package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

public class OrderRequest {

    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    private OrderRequest() {
    }

    public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity() {
        List<OrderLineItem> orderLineItems = this.orderLineItems.stream()
                .map(OrderLineItemRequest::toEntity)
                .collect(Collectors.toList());

        return new Order(orderTableId, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
