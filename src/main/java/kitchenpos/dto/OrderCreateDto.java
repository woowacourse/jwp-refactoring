package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderCreateDto {

    private final Long orderTableId;
    private final List<OrderLineItemCreateDto> orderLineItems;

    public OrderCreateDto(final Long orderTableId, final List<OrderLineItemCreateDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toDomain() {
        final List<OrderLineItem> orderLineItemDomains = orderLineItems.stream()
                .map(OrderLineItemCreateDto::toDomain)
                .collect(Collectors.toList());
        return new Order(orderTableId, orderLineItemDomains);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateDto> getOrderLineItems() {
        return orderLineItems;
    }
}
