package kitchenpos.application.dto;

import java.util.List;
import java.util.stream.Collectors;

public class CreateOrderDto {

    private Long orderTableId;
    private List<CreateOrderLineItemDto> orderLineItems;

    private CreateOrderDto() {
    }

    public CreateOrderDto(final Long orderTableId,
                          final List<CreateOrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<CreateOrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getMenuIds() {
        return getOrderLineItems().stream()
                .map(CreateOrderLineItemDto::getMenuId)
                .collect(Collectors.toList());
    }
}
