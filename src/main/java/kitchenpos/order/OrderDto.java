package kitchenpos.order;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public static OrderDto from(Order order) {
        return new OrderDto(order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(), order.getOrderLineItems());
    }

    public OrderDto(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order toDomain() {
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
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
