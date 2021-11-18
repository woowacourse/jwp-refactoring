package kitchenpos.order.domain;

import org.springframework.context.ApplicationEvent;

public class OrderStatusCheckEvent extends ApplicationEvent {

    private final OrderTable orderTable;

    public OrderStatusCheckEvent(Object source) {
        super(source);
        this.orderTable = (OrderTable) source;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }
}
