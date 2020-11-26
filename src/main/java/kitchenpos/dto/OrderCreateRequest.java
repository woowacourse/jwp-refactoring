package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItem> orderLineItemRequests;

    public OrderCreateRequest() {
    }

    private OrderCreateRequest(Long orderTableId,
        List<OrderLineItem> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public static OrderCreateRequest of(Order order, List<OrderLineItem> orderLineItems) {
        OrderTable orderTable = order.getOrderTable();

        return new OrderCreateRequest(orderTable.getId(), orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItemRequests;
    }
}
