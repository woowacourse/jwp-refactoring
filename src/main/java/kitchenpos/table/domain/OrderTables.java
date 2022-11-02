package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderTables {
    private static final int MINIMUM_GROUP_TABLE_COUNTS = 2;

    private List<OrderTable> orderTables;

    private OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public boolean hasValidOrderTableSize(final List<OrderTable> registeredTables) {
        return getOrderTableIds().size() == registeredTables.size();
    }

    public void groupTables(final Long tableGroupId) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeGroupTable(tableGroupId);
        }
    }

    public boolean isGroupAble() {
        return orderTables.size() >= MINIMUM_GROUP_TABLE_COUNTS
                && orderTables.stream()
                .allMatch(OrderTable::isGroupAble);
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
