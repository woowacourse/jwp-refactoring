package kitchenpos.order.domain;

import org.springframework.context.ApplicationEvent;

public class OrderCompletedEvent extends ApplicationEvent {

    private final Long orderTableId;

    public OrderCompletedEvent(final Object source, final Long orderTableId) {
        super(source);
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
