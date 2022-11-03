package kitchenpos.order.domain;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import kitchenpos.order.dao.OrderDao;

@Component
public class TableChangeEmptyValidatorImpl implements TableChangeEmptyValidator {

    private final OrderDao orderDao;

    public TableChangeEmptyValidatorImpl(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public void validate(final OrderTable orderTable) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("cannot ungroup: order status not completion exist");
        }
    }
}
