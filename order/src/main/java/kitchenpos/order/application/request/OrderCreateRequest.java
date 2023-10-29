package kitchenpos.order.application.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.dto.OrderLineItemDto;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemDto> orderLineItems;

    protected OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public List<OrderLineItem> convertToOrderLineItems() {
        return orderLineItems.stream()
                .map(this::createOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem createOrderLineItem(OrderLineItemDto orderLineItemDto) {
        return OrderLineItem.builder()
                .menuId(orderLineItemDto.getMenuId())
                .quantity(orderLineItemDto.getQuantity())
                .menuName(orderLineItemDto.getMenuName())
                .price(orderLineItemDto.getPrice())
                .build();
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }
}
