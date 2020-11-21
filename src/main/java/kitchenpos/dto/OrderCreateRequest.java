package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderCreateRequest() {
    }

    private OrderCreateRequest(Long orderTableId,
        List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public static OrderCreateRequest of(Order order) {
        OrderTable orderTable = order.getOrderTable();
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        return new OrderCreateRequest(orderTable.getId(),
            OrderLineItemRequest.toRequestList(orderLineItems));
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
