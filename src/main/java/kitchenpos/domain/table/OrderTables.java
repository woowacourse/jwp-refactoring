package kitchenpos.domain.table;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.tablegroup.TableGroup;

public class OrderTables {

    private List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void validateGroupingTable() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.validateGroupingTable();
            orderTable.validateEmptyTable();
        }
    }

    public void validateOrderTableCount(final List<Long> orderTableIds) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public void updateTableGroup(final TableGroup tableGroup) {
        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.updateTableGroup(tableGroup);
            savedOrderTable.updateEmpty(false);
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
