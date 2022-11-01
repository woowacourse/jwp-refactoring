package kitchenpos.application.dto;

import java.util.List;
import java.util.stream.Collectors;

public class CreateOrderDto {

    private final Long orderTableId;
    private final List<CreateOrderLineItemDto> orderLineItems;

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
