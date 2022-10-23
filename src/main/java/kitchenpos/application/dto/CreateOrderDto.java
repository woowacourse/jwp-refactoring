package kitchenpos.application.dto;

import java.util.List;

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
}
