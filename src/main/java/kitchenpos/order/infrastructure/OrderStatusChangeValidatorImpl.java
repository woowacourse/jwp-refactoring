package kitchenpos.order.infrastructure;

import java.util.Arrays;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.InvalidOrderException;
import kitchenpos.table.domain.OrderStatusChangeValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusChangeValidatorImpl implements OrderStatusChangeValidator {

    private final OrderDao orderDao;

    public OrderStatusChangeValidatorImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public void validate(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new InvalidOrderException("주문이 완료 상태가 아닙니다.");
        }
    }
}
