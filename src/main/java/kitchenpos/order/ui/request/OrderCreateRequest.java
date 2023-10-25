package kitchenpos.order.ui.request;

import java.util.List;
import javax.validation.constraints.NotNull;

public class OrderCreateRequest {

    @NotNull
    private final Long orderTableId;
    @NotNull
    private final List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
