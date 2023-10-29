package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

import java.util.List;
import java.util.stream.Collectors;

public class CreateOrderDto {

    private final Long orderTableId;
    private final List<CreateOrderLineItemDto> orderLineItems;

    public CreateOrderDto(final Long orderTableId, final List<CreateOrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderLineItems toOrderLineItems() {
        final List<OrderLineItem> orderLineItemEntities = orderLineItems.stream()
                                                          .map(CreateOrderLineItemDto::toOrderLineItem)
                                                          .collect(Collectors.toList());

        return new OrderLineItems(orderLineItemEntities);
    }
}
