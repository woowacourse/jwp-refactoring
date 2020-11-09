package kitchenpos.domain.model.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private final Long id;
    private final Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private final List<OrderLineItem> orderLineItems;

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order create(OrderCreateService orderCreateService) {
        orderCreateService.validate(orderLineItems, orderTableId);

        this.orderStatus = OrderStatus.COOKING.name();
        this.orderedTime = LocalDateTime.now();
        return this;
    }

    public Order changeOrderStatus(String orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
        return this;
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
