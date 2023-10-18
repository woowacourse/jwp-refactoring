package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderResponse {

    private final Long id;
    private final OrderStatus orderStatus;
    private final TableResponse orderTable;
    private final List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(
            Long id,
            OrderStatus orderStatus,
            TableResponse orderTable,
            List<OrderLineItemResponse> orderLineItems
    ) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderStatus(),
                TableResponse.from(order.getOrderTable()),
                OrderLineItemResponse.from(order.getOrderLineItems())
        );
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public TableResponse getOrderTable() {
        return orderTable;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
