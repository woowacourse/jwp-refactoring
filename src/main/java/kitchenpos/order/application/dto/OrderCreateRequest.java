package kitchenpos.order.application.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;

public class OrderCreateRequest {

    private final Long orderTableId;

    @NotEmpty
    private final List<OrderLineItemRequest> orderLineItems;

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
