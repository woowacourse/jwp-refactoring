package kitchenpos.table.application;

import kitchenpos.order.application.OrderValidator;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;

@Service
public class OrderValidatorImpl implements OrderValidator {

    private final OrderTableDao orderTableDao;

    public OrderValidatorImpl(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Override
    public void validateEmpty(Long orderId) {
        final OrderTable orderTable = findOrderTable(orderId);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
