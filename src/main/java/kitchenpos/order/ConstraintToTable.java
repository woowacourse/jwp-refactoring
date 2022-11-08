package kitchenpos.order;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableRule;
import kitchenpos.table.domain.entity.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class ConstraintToTable implements TableRule {

    private OrderService orderService;

    public ConstraintToTable(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public boolean unableToChangeEmpty(OrderTable orderTable) {
        return orderService.isNotAllOrderFinish(orderTable);
    }
}
