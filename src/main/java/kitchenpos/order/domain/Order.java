package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;

import java.time.LocalDateTime;
import kitchenpos.order.exception.OrderStatusChangeFailedException;

public class Order {

    private final Long id;
    private final Long orderTableId;
    private OrderStatus orderStatus;
    private final LocalDateTime orderedTime;


    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(null, orderTableId, orderStatus, orderedTime);
    }

    public static Order from(Long orderTableId) {
        return new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now());
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateNotCompletion();
        this.orderStatus = orderStatus;
    }

    private void validateNotCompletion() {
        if (COMPLETION.equals(orderStatus)) {
            throw new OrderStatusChangeFailedException();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
