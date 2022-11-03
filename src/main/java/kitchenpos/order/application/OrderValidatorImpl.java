package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.application.OrderValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private static final List<String> CRITERIA_ORDER_STATUS = Arrays.asList(COOKING.name(), MEAL.name());

    private final OrderDao orderDao;

    public OrderValidatorImpl(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public void ValidateOrderTableId(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, CRITERIA_ORDER_STATUS)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void ValidateOrderTableIds(final List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, CRITERIA_ORDER_STATUS)) {
            throw new IllegalArgumentException();
        }
    }
}
