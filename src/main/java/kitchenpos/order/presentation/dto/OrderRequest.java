package kitchenpos.order.presentation.dto;

import java.util.List;
import kitchenpos.order.application.dto.OrderRequestDto;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest(Long orderTableId,
                        List<OrderLineItemRequest> orderLineItemRequestList) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequestList;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public OrderRequestDto toServiceDto(){
        return new OrderRequestDto(orderTableId, orderLineItemRequests);
    }
}
