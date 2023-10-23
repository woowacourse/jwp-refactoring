package kitchenpos.application.request;

import java.util.List;
import kitchenpos.application.dto.OrderLineItemDto;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemDto> orderLineItems;

    protected OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemDto> orderLineItems) {
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
