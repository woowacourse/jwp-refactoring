package kitchenpos.order.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderLineItemSaveRequest;
import kitchenpos.order.application.dto.OrderSaveRequest;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderSaveRequest toRequest() {
        List<OrderLineItemSaveRequest> orderLineItemSaveRequests = orderLineItems.stream()
            .map(OrderLineItemCreateRequest::toRequest)
            .collect(Collectors.toList());
        return new OrderSaveRequest(orderTableId, orderLineItemSaveRequests);
    }
}
