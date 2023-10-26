package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTables;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidator implements OrderTableUpGroupValidator {

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
