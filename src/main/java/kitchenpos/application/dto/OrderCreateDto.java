package kitchenpos.application.dto;

import java.util.List;

public class OrderCreateDto {

    private final Long orderTableId;
    private final List<OrderLineItemDto> orderLineItemDtos;

    public OrderCreateDto(final Long orderTableId, final List<OrderLineItemDto> orderLineItemDtos) {
        this.orderTableId = orderTableId;
        this.orderLineItemDtos = orderLineItemDtos;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItemDtos() {
        return orderLineItemDtos;
    }
}
