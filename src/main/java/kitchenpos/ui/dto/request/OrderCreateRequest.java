package kitchenpos.ui.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderCreateRequestDto;
import kitchenpos.application.dto.request.OrderLineItemRequestDto;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public OrderCreateRequestDto toDto() {
        return new OrderCreateRequestDto(orderTableId, convert());
    }

    private List<OrderLineItemRequestDto> convert() {
        return orderLineItemRequests.stream()
            .map(OrderLineItemRequest::toDto)
            .collect(Collectors.toList());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
