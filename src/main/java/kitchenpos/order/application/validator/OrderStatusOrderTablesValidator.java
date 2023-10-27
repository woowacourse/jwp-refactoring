package kitchenpos.order.application.validator;

import java.util.Arrays;
import kitchenpos.order.application.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.application.OrderTablesChangingEmptinessValidator;
import kitchenpos.ordertable.domain.OrderTable;
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
