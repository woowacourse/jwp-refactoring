package kitchenpos.application.dto;

import java.util.List;

public class CreateOrderDto {

    private Long orderTableId;
    private List<CreateOrderLineItemDto> orderLineItems;

    public CreateOrderDto() {
    }

    public CreateOrderDto(Long orderTableId, List<CreateOrderLineItemDto> orderLineItems) {
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
