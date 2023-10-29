package kitchenpos.core.order.application.validator;

import java.util.Arrays;
import java.util.List;
import kitchenpos.core.order.application.OrderDao;
import kitchenpos.core.order.domain.OrderStatus;
import kitchenpos.core.ordertable.domain.OrderTableUngroupValidator;
import kitchenpos.core.ordertable.domain.OrderTables;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidator implements OrderTableUngroupValidator {

    private final OrderDao orderDao;

    public OrderStatusValidator(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public void validate(final OrderTables orderTables) {
        final List<Long> orderTableIds = orderTables.getOrderTableIds();

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
