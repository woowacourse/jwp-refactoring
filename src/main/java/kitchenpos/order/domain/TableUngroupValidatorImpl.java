package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;

@Component
public class TableUngroupValidatorImpl implements TableUngroupValidator {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableUngroupValidatorImpl(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Override
    public void validate(final TableGroup tableGroup) {
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroup.getId());
        List<Long> orderTableIds = getOrderTableIds(orderTables);
        validateOrdersNotCompletion(orderTableIds);
    }

    public void validateOrdersNotCompletion(final List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("cannot ungroup: order status not completion exist");
        }
    }

    private List<Long> getOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toUnmodifiableList());
    }
}
