package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateOrderDto;

public class CreateOrderRequest {

    private Long orderTableId;
    private List<CreateOrderLineItemRequest> orderLineItems;

    protected CreateOrderRequest() {
    }

    public CreateOrderRequest(final Long orderTableId,
                              final List<CreateOrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public CreateOrderDto toCreateOrderDto() {
        return new CreateOrderDto(orderTableId, orderLineItems.stream()
                .map(CreateOrderLineItemRequest::toCreateOrderLineItemDto)
                .collect(Collectors.toList()));
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<CreateOrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
