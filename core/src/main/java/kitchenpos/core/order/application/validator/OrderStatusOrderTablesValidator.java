package kitchenpos.core.order.application.validator;

import java.util.Arrays;
import kitchenpos.core.order.application.OrderDao;
import kitchenpos.core.order.domain.OrderStatus;
import kitchenpos.core.ordertable.application.OrderTablesChangingEmptinessValidator;
import kitchenpos.core.ordertable.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusOrderTablesValidator implements OrderTablesChangingEmptinessValidator {
    private final OrderDao orderDao;

    public OrderStatusOrderTablesValidator(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public void validate(final OrderTable orderTable) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
