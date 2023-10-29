package kitchenpos.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemCreateRequest> orderLineItems = new ArrayList<>();

    protected OrderCreateRequest() {
    }

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
