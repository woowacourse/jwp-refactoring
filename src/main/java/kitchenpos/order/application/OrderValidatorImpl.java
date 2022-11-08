package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.OrderValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private static final List<String> CONDITIONS = Arrays.asList(
            OrderStatus.COOKING.name(),
            OrderStatus.MEAL.name()
    );

    private final OrderDao orderDao;

    public OrderValidatorImpl(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public void validateNotCookingAndMeal(final List<Long> orderTableIds) {
        boolean haveConditions = orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, CONDITIONS);
        if (haveConditions) {
            throw new IllegalArgumentException();
        }
    }
}
