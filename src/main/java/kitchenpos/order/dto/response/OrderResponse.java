package kitchenpos.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.order.domain.Order;

public class OrderResponse {

    private Long id;
    private OrderTableResponse orderTableResponse;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse() {
    }

    private OrderResponse(
            Long id,
            OrderTableResponse orderTableResponse,
            String orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItemResponse> orderLineItems
    ) {
        this.id = id;
        this.orderTableResponse = orderTableResponse;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                OrderTableResponse.from(order.getOrderTable()),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                order.getOrderLineItems()
                        .stream()
                        .map(OrderLineItemResponse::from)
                        .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTableResponse() {
        return orderTableResponse;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
