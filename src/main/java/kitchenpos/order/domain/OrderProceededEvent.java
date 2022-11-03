package kitchenpos.order.domain;

import org.springframework.context.ApplicationEvent;

public class OrderProceededEvent extends ApplicationEvent {

    private final Long orderTableId;

    public OrderProceededEvent(final Object source, final Long orderTableId) {
        super(source);
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
