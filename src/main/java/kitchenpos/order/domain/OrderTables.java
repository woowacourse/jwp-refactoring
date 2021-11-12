package kitchenpos.order.domain;

import kitchenpos.event.OrderTableUngroupEventPublisher;

import java.util.List;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void ungroupAll(OrderTableUngroupEventPublisher orderTableUngroupEventPublisher) {
        orderTables.forEach(orderTableUngroupEventPublisher::publish);
        ungroup();
    }

    private void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }
}
