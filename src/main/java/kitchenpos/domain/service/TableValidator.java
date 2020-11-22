package kitchenpos.domain.service;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {
    private OrderDao orderDao;

    public TableValidator(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void validateOrderStatusOfTable(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.getUnmodifiableStatus())) {
            throw new IllegalArgumentException();
        }
    }
}
