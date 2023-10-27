package kitchenpos.order.dto.request;

import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderCreateRequest {

    @NotNull
    private final Long orderTableId;

    @NotNull
    private final List<OrderLineItemsCreateRequest> orderLineItems;

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemsCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemsCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
