package kitchenpos.order;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.collection.Orders;
import kitchenpos.table.application.TableGroupRule;
import kitchenpos.table.domain.collection.OrderTables;
import org.springframework.stereotype.Component;

@Component
public class ConstraintToTableGroup implements TableGroupRule {

    private OrderService orderService;

    public ConstraintToTableGroup(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public boolean unableToUngroup(OrderTables orderTables) {
        Orders orders = orderService.findOrdersInOrderTables(orderTables);
        return !orders.isAllCompleted();
    }
}
