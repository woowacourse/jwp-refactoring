package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.util.CollectionUtils;

public class Order {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);

        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(this.orderStatus, OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException();
        }

        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
