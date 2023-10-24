package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupValidator {
    private static final int ORDER_TABLE_MIN_SIZE = 2;

    private final OrderTableDao orderTableDao;

    public TableGroupValidator(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    public void validate(final TableGroup tableGroup) {
        validateOrderTablesIsExist(tableGroup.getOrderTables());
        validateOrderTablesSize(tableGroup.getOrderTables());
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < ORDER_TABLE_MIN_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesIsExist(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }

    }
}
