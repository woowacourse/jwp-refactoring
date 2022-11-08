package kitchenpos.order.application;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.validator.TableChangeEmptyValidator;
import kitchenpos.table.domain.OrderTable;

@Component
public class TableChangeEmptyValidatorImpl implements TableChangeEmptyValidator {

    private final OrderDao orderDao;

    public TableChangeEmptyValidatorImpl(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public void validate(final OrderTable orderTable) {
        validateOrderStatusCompletion(orderTable);
        validateOrderNotGrouped(orderTable);
    }

    private void validateOrderStatusCompletion(final OrderTable orderTable) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("cannot ungroup: order status not completion exist");
        }
    }

    private void validateOrderNotGrouped(final OrderTable orderTable) {
        if (orderTable.getTableGroupId() != null) {
            throw new IllegalArgumentException("could not change empty, table is now grouped");
        }
    }
}
