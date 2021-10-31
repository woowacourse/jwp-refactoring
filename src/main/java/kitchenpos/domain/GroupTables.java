package kitchenpos.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.domain.validator.TableGroupValidator;
import org.springframework.util.CollectionUtils;

public class GroupTables {
    private static final int GROUP_TABLE_MIN_SIZE = 2;
    private List<OrderTable> orderTables;

    public GroupTables(List<OrderTable> orderTables) {
        validateTableGroupSize(orderTables);
        validateDistinctTables(orderTables);
        this.orderTables = orderTables;
    }

    private void validateTableGroupSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < GROUP_TABLE_MIN_SIZE) {
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

    public boolean canAssignTableGroup() {
        return orderTables.stream().allMatch(OrderTable::canAssignTableGroup);
    }

    public void setTableGroup(TableGroup tableGroup) {
        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.setTableGroup(tableGroup);
            savedOrderTable.setEmpty(false);
        }
    }

    public void ungroup(TableGroupValidator tableGroupValidator) {
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
