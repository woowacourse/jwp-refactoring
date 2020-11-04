package kitchenpos.domain;

import kitchenpos.domain.exceptions.InvalidOrderTableSizesException;
import kitchenpos.domain.exceptions.TableAlreadyHasGroupException;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class OrderTables {

    private final List<OrderTable> orderTables;

    private OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(final List<Long> orderTableIds, final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new InvalidOrderTableSizesException();
        }

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }

        return new OrderTables(orderTables);
    }

    public static List<OrderTable> validatedForGrouping(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables, final TableGroup tableGroup) {
        OrderTables orderTables = OrderTables.of(orderTableIds, savedOrderTables);
        for (final OrderTable savedOrderTable : orderTables.orderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new TableAlreadyHasGroupException();
            }
            savedOrderTable.changeEmptyState(false);
            savedOrderTable.setTableGroup(tableGroup);
        }
        return orderTables.orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
