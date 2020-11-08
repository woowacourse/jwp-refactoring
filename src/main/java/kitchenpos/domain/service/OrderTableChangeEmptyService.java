package kitchenpos.domain.service;

import static java.util.Arrays.*;
import static java.util.Objects.*;

import org.springframework.stereotype.Service;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.repository.OrderDao;

@Service
public class OrderTableChangeEmptyService {
    private final OrderDao orderDao;

    public OrderTableChangeEmptyService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void validate(Long id, Long tableGroupId) {
        if (nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(id,
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
