package kitchenpos.ui.dto.response;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderResponse {

    private Long id;
    private Long orderTable;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    private OrderResponse() {
    }

    private OrderResponse(Long id, Long orderTable, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse create(Order order, List<OrderLineItem> orderLineItems) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                OrderLineItemResponse.of(orderLineItems)
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTable() {
        return orderTable;
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
