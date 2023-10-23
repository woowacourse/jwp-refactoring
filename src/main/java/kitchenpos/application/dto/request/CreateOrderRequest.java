package kitchenpos.application.dto.request;

import java.util.List;

public class CreateOrderRequest {

    private Long orderTableId;
    private List<OrderLineItemDto> orderLineItems;

    protected CreateOrderRequest() {
    }

    public CreateOrderRequest(Long orderTableId, List<OrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }
}
