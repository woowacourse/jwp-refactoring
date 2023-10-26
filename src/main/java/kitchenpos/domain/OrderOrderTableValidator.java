package kitchenpos.domain;

import kitchenpos.dao.OrderTableDao;
import org.springframework.stereotype.Component;

@Component
public class OrderOrderTableValidator implements OrderValidator {

    private final OrderTableDao orderTableDao;

    public OrderOrderTableValidator(final OrderTableDao orderTableDao) {
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
