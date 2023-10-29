package kitchenpos.order.application.dto;

import java.util.List;

public class OrderCreateDto {

    private final Long orderTableId;
    private final List<OrderLineItemCreateDto> orderLineItems;

    public OrderCreateDto(final Long orderTableId, final List<OrderLineItemCreateDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateDto> getOrderLineItems() {
        return orderLineItems;
    }
}
