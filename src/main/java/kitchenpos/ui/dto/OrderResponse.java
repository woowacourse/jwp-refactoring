package kitchenpos.ui.dto;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(), order.getOrderTable().getId(), order.getOrderStatus(),
                order.getOrderedTime(), order.getOrderLineItems());
    }

    public OrderResponse(Long id,
                         Long orderTableId,
                         String orderStatus,
                         LocalDateTime orderedTime,
                         List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
