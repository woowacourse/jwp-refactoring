package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.application.OrderStatusValidator;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusValidatorImpl implements OrderStatusValidator {

    private final OrderDao orderDao;

    public OrderStatusValidatorImpl(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }


    @Override
    public boolean existsByIdAndStatusNotCompletion(final Long orderTableId) {
        return orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, OrderStatus.getCookingAndMealStatusNames());
    }

    @Override
    public boolean existsByOrderTableIdInAndStatusNotCompletion(final List<Long> orderTableIds) {
        return orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, OrderStatus.getCookingAndMealStatusNames());

    }
}
