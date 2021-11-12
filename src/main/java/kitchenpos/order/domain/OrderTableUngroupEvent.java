package kitchenpos.order.domain;

import org.springframework.context.ApplicationEvent;

public class OrderTableUngroupEvent extends ApplicationEvent {

    private final OrderTable orderTable;

    public OrderTableUngroupEvent(Object source) {
        super(source);
        this.orderTable = (OrderTable) source;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }
}
