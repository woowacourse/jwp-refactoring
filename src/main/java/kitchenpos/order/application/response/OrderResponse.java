package kitchenpos.order.application.response;

import kitchenpos.table.application.response.OrderTableResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private long id;
    private OrderTableResponse orderTable;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(final long id,
                         final OrderTableResponse orderTable,
                         final OrderStatus orderStatus,
                         final LocalDateTime orderedTime,
                         final List<OrderLineItemResponse> orderLineItems
    ) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(final Order order, final OrderTable orderTable, final List<Menu> menus) {
        return new OrderResponse(
                order.getId(),
                OrderTableResponse.from(orderTable),
                order.getOrderStatus(),
                order.getOrderedTime(),
                OrderLineItemResponse.from(order.getOrderLineItems(), menus)
        );
    }

    public long getId() {
        return id;
    }

    public OrderTableResponse getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
