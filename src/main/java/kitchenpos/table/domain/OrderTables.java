package kitchenpos.table.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.table.service.TableValidator;

public class OrderTables {
    private static final int GROUP_TABLE_MIN_SIZE = 2;
    private List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        validateTableGroupSize(orderTables);
        validateDistinctTables(orderTables);
        this.orderTables = orderTables;
    }

    private void validateTableGroupSize(List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < GROUP_TABLE_MIN_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    private void validateDistinctTables(List<OrderTable> orderTables) {
        Set<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toSet());

        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }
    }

    public void assign(TableGroup tableGroup) {
        validateAssignGroup();
        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.setTableGroup(tableGroup);
            savedOrderTable.setEmpty(false);
        }
    }

    private void validateAssignGroup() {
        if (!orderTables.stream().allMatch(OrderTable::canAssignTableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup(TableValidator tableGroupValidator) {
        tableGroupValidator.validateUngroup(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(null);
            orderTable.setEmpty(false);
        }
    }

    public List<OrderTable> toList() {
        return orderTables;
    }
}
