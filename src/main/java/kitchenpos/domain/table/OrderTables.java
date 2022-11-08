package kitchenpos.domain.table;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        validateOverTwo(orderTables);
        this.orderTables = orderTables;
    }

    private void validateOverTwo(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public boolean canGroup(List<Long> orderTableIds) {
        if (!isSameSize(orderTableIds)) {
            return false;
        }
        return this.orderTables.stream()
                .noneMatch(each -> each.getTableGroupId() != null || !each.isEmpty());
    }

    private boolean isSameSize(List<Long> orderTableIds) {
        return orderTableIds.size() == this.orderTables.size();
    }

    public OrderTables changeTableGroupId(Long tableGroupId) {
        List<OrderTable> changedOrderTables = this.orderTables.stream()
                .map(orderTable -> orderTable.changeTableGroupId(tableGroupId))
                .collect(Collectors.toUnmodifiableList());
        return new OrderTables(changedOrderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toUnmodifiableList());
    }
}
