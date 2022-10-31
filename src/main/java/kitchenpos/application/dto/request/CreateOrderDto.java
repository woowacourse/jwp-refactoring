package kitchenpos.application.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderLineItem;

public class CreateOrderDto {

    private final Long orderTableId;
    private final List<CreateOrderLineItemDto> orderLineItems;

    public CreateOrderDto(Long orderTableId,
                          List<CreateOrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<CreateOrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }

    public List<OrderLineItem> toOrderLineItem(Long orderId) {
        return orderLineItems.stream()
                .map(it -> new OrderLineItem(orderId, it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
    }
}
