package kitchenpos.dto.order;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.orderlineitem.OrderLineItemRequest;

public class OrderRequest {

    private final OrderStatus orderStatus;
    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
        this(null, null, null);
    }

    public OrderRequest(OrderStatus orderStatus) {
        this(orderStatus, null, null);
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this(null, orderTableId, orderLineItemRequests);
    }

    public OrderRequest(
        OrderStatus orderStatus,
        Long orderTableId,
        List<OrderLineItemRequest> orderLineItems
    ) {
        this.orderStatus = orderStatus;
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
