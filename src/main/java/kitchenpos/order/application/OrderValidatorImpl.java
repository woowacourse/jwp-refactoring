package kitchenpos.order.application;

import kitchenpos.table.application.OrderValidator;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private final List<String> cookingAndMeal = List.of(
            OrderStatus.COOKING.name(),
            OrderStatus.MEAL.name()
    );

    private final OrderDao orderDao;

    public OrderValidatorImpl(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public void validateCompletion(final List<Long> orderTableIds) {
        boolean haveConditions = orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, cookingAndMeal);
        if (haveConditions) {
            throw new IllegalArgumentException();
        }
    }
}
