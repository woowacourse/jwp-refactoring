package kitchenpos.core.order.application.validator;

import kitchenpos.core.order.application.OrderCreationValidator;
import kitchenpos.core.order.domain.Order;
import kitchenpos.core.ordertable.application.OrderTableDao;
import kitchenpos.core.ordertable.domain.OrderTable;
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
