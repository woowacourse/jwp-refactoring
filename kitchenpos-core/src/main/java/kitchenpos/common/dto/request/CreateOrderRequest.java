package kitchenpos.common.dto.request;

import java.util.List;

public class CreateOrderRequest {

    private Long orderTableId;
    private List<OrderLineItemDto> orderLineItems;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(final Long orderTableId, final List<OrderLineItemDto> orderLineItems) {
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
