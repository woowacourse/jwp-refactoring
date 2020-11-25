package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.dto.OrderLineItemDto;

public class OrderLineItems {

    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(Order order, List<OrderLineItemDto> orderLineItemDtos) {
        validate(orderLineItemDtos);
        this.orderLineItems = orderLineItemDtos.stream()
            .map(orderLineItemDto -> createOrderLineItem(order, orderLineItemDto))
            .collect(Collectors.toList());
    }

    private void validate(List<OrderLineItemDto> orderLineItemDtos) {
        long menuCount = orderLineItemDtos.stream()
            .map(OrderLineItemDto::getMenuId)
            .distinct()
            .count();

        if (menuCount != orderLineItemDtos.size()) {
            throw new IllegalArgumentException();
        }
    }

    private OrderLineItem createOrderLineItem(Order order, OrderLineItemDto orderLineItemDto) {
        return new OrderLineItem(order, orderLineItemDto.getMenuId(), orderLineItemDto.getQuantity());
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
