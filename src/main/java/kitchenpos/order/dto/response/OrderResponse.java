package kitchenpos.order.dto.response;

import java.util.List;
import kitchenpos.table.dto.response.TableResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

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

    public static OrderResponse from(
            Order order,
            TableResponse tableResponse,
            List<OrderLineItemResponse> orderLineItems
    ) {
        return new OrderResponse(
                order.getId(),
                order.getOrderStatus(),
                tableResponse,
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
