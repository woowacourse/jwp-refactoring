package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderRequest {

    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public OrderRequest(Long orderTableId, String orderStatus, LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity() {
        return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Order toEntity(Long id) {
        Order order = new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
        order.setId(id);
        return order;
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
