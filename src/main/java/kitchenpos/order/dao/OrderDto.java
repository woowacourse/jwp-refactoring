package kitchenpos.order.dao;

import java.time.LocalDateTime;
import kitchenpos.order.domain.Order;

public class OrderDto {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;

    public OrderDto(final Order order) {
        this(order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime());
    }

    public OrderDto(final Long id, final Long orderTableId, final String orderStatus,
                    final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order toEntity() {
        return new Order(id, orderTableId, orderStatus, orderedTime);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
