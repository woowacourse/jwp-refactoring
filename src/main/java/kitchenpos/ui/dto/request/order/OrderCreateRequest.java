package kitchenpos.ui.dto.request.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.order.OrderCreateRequestDto;
import kitchenpos.application.dto.request.order.OrderLineItemRequestDto;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderCreateRequestDto toDto() {
        return new OrderCreateRequestDto(orderTableId, convert());
    }

    private List<OrderLineItemRequestDto> convert() {
        return orderLineItems.stream()
            .map(OrderLineItemRequest::toDto)
            .collect(Collectors.toList());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
