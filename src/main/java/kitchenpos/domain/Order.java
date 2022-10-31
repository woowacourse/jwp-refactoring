package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private final Long id;
    private final Long orderTableId;
    private String orderStatus;
    private final LocalDateTime orderedTime;

    public Order(final Long id, final Long orderTableId, final String orderStatus,
        final LocalDateTime orderedTime
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(
        final Long orderTableId,
        final String orderStatus,
        final LocalDateTime orderedTime
    ) {
        this(null, orderTableId, orderStatus, orderedTime);
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

    public void updateOrderStatus(final String orderStatus) {
        validateNotCompletionNow();

        this.orderStatus = orderStatus;
    }

    private void validateNotCompletionNow() {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new IllegalArgumentException();
        }
    }
}
