package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItems;

    @JsonCreator
    public OrderCreateRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
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
