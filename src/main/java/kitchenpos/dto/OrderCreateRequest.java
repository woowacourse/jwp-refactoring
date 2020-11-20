package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemCreateRequest> orderLineItemCreateRequests;

    public OrderCreateRequest() {
    }

    private OrderCreateRequest(Long orderTableId,
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemCreateRequests = orderLineItemCreateRequests;
    }

    public static OrderCreateRequest of(Order order) {
        OrderTable orderTable = order.getOrderTable();
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        return new OrderCreateRequest(orderTable.getId(),
            OrderLineItemCreateRequest.toRequestList(orderLineItems));
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItemCreateRequests() {
        return orderLineItemCreateRequests;
    }
}
