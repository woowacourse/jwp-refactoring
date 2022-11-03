package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.util.List;
import kitchenpos.order.domain.dao.OrderDao;
import kitchenpos.order.exception.InvalidChangedEmptyRequest;
import kitchenpos.table.application.OrderStatusValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidatorImp implements OrderStatusValidator {

    private final OrderDao orderDao;

    public OrderStatusValidatorImp(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public void validateChangeEmpty(Long orderTableId) {
        if (shouldNotChangeEmpty(orderTableId)) {
            throw new InvalidChangedEmptyRequest();
        }
    }

    private boolean shouldNotChangeEmpty(Long orderTableId) {
        return orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(COOKING.name(), MEAL.name()));
    }
}
