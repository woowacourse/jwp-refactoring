package kitchenpos.order.domain;

import kitchenpos.event.OrderStatusCheckEventPublisher;

import java.util.List;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void ungroupAll(OrderStatusCheckEventPublisher orderStatusCheckEventPublisher) {
        orderTables.forEach(orderStatusCheckEventPublisher::publish);
        ungroup();
    }

    private void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }
}
