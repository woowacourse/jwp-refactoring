package kitchenpos.application.request;

import java.util.List;
import kitchenpos.domain.OrderStatus;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final List<OrderLineItemCreateRequest> orderLineItems;
    private final OrderStatus orderStatus;

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemCreateRequest> orderLineItems,
                              OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
