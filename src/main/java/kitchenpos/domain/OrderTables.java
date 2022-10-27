package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderTables {
    private static final int MINIMUM_GROUP_TABLE_COUNTS = 2;

    private final List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public boolean hasValidOrderTables(final long registeredTableCounts) {
        return getOrderTableIds().size() == registeredTableCounts;
    }

    public void groupTables(final Long tableGroupId) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeGroupTable(tableGroupId);
        }
    }

    public boolean isGroupAble() {
        return !isEmpty()
                && orderTables.size() >= MINIMUM_GROUP_TABLE_COUNTS
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

    public void addOrderTable(final OrderTable... orderTables) {
        this.orderTables.addAll(Arrays.asList(orderTables));
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
