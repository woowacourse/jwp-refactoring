package kitchenpos.order.dto.response;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dto.response.TableResponse;

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

    public static OrderResponse from(Order order, List<OrderLineItemResponse> orderLineItems) {
        return new OrderResponse(
                order.getId(),
                order.getOrderStatus(),
                TableResponse.from(order.getOrderTable()),
                orderLineItems
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
