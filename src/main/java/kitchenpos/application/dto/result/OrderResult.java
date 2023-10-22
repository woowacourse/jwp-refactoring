package kitchenpos.application.dto.result;

import java.time.LocalDateTime;
import kitchenpos.domain.order.Order;

public class OrderResult {

    private final Long id;
    private final String orderStatus;
    private final LocalDateTime orderedTime;

    public OrderResult(
            final Long id,
            final String orderStatus,
            final LocalDateTime orderedTime
    ) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static OrderResult from(final Order order) {
        return new OrderResult(
                order.getId(),
                order.getOrderStatus().name(),
                order.getOrderedTime()
        );
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
