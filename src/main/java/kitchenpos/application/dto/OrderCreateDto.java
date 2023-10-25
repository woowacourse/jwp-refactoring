package kitchenpos.application.dto;

import java.util.List;

public class OrderCreateDto {

    private final Long orderTableId;
    private final List<OrderLineItemDto> orderLineItems;

    public OrderCreateDto(final Long orderTableId, final List<OrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }
}
