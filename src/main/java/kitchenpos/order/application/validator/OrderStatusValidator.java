package kitchenpos.order.application.validator;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.application.OrderDao;
import kitchenpos.ordertable.domain.OrderTableUngroupValidator;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTables;
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
