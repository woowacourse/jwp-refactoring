package kitchenpos.table.domain;

import org.springframework.context.ApplicationEvent;

public class OrderTableChangeEvent extends ApplicationEvent {

    private final transient OrderTable orderTable;

    public OrderTableChangeEvent(OrderTable orderTable) {
        super(orderTable);
        this.orderTable = orderTable;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
