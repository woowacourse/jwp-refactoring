package kitchenpos.dto.request;

import com.sun.istack.NotNull;
import java.util.List;

public class CreateOrderRequest {

    @NotNull
    private Long orderTableId;
    @NotNull
    private List<OrderLineItemRequest> orderLineItems;

    public CreateOrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
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
