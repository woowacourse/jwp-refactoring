package kitchenpos.order.application;

import kitchenpos.ordertable.application.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderCreationOrderTableValidator implements OrderCreationValidator {

    private final OrderTableDao orderTableDao;

    public OrderCreationOrderTableValidator(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Override
    public void validate(final Order order) {
        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
