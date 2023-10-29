package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

public class CreateOrderRequest {

    private Long orderTableId;
    private List<CreateOrderLineItemRequest> orderLineItems;

    public CreateOrderRequest() {
    }

    public CreateOrderDto toCreateOrderDto() {
        final List<CreateOrderLineItemDto> dtos = orderLineItems.stream()
                                                                   .map(CreateOrderLineItemRequest::toCreateOrderLineItemDto)
                                                                   .collect(Collectors.toList());
        return new CreateOrderDto(orderTableId, dtos);
    }
}
