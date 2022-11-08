package kitchenpos.order.application.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.validator.TableUngroupValidator;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

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
        validateOrderTablesExist(tableGroup);
        validateOrdersNotCompletion(getOrderTableIds(tableGroup));
    }

    private void validateOrderTablesExist(final TableGroup tableGroup) {
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroup.getId());
        if (orderTables.size() != tableGroup.getOrderTables().size()) {
            throw new IllegalArgumentException("order tables not completely exist");
        }
    }

    public void validateOrdersNotCompletion(final List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("cannot ungroup: order status not completion exist");
        }
    }

    private List<Long> getOrderTableIds(final TableGroup tableGroup) {
        return tableGroup.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toUnmodifiableList());
    }
}
