package kitchenpos.order.application.dto;

import java.util.List;
import kitchenpos.order.presentation.dto.OrderLineItemRequest;

public class OrderRequestDto {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequestDto(final Long orderTableId,
                           final List<OrderLineItemRequest> orderLineItemRequestList) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequestList;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItemRequests;
    }
}
