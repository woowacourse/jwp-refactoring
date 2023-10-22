package kitchenpos.ui.dto;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CreateOrderRequest {

    private Long orderTableId;
    private List<CreateOrderLineItemRequest> orderLineItems;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(final Long orderTableId, final List<CreateOrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<CreateOrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Order toEntity() {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        final List<OrderLineItem> orderLineItems = this.orderLineItems.stream()
                                                                      .map(CreateOrderLineItemRequest::toEntity)
                                                                      .collect(toList());
        order.setOrderLineItems(orderLineItems);

        return order;
    }
}
