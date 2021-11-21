package kitchenpos.application.dto.response.order;

import java.time.LocalDateTime;
import kitchenpos.application.dto.response.table.OrderTableResponse;
import kitchenpos.domain.order.Order;

public class OrderResponse {

    private Long id;
    private OrderTableResponse orderTableResponse;
    private String OrderStatus;
    private LocalDateTime orderedTime;

    public OrderResponse(Long id,
            OrderTableResponse orderTableResponse, String orderStatus,
            LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableResponse = orderTableResponse;
        this.OrderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static OrderResponse create(Order order) {
        return new OrderResponse(
                order.getId(),
                OrderTableResponse.create(order.getOrderTable()),
                order.getOrderStatus(),
                order.getOrderedTime()
        );
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTableResponse() {
        return orderTableResponse;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
