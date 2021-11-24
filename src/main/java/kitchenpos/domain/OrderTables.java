package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {
    private static final int MINIMUM_NUMBER_OF_GROUP_TABLE = 2;

    private final List<OrderTable> values;

    public OrderTables(List<OrderTable> values) {
        this.values = values;
    }
    
    public void validateGroupingNumbers() {
        if (CollectionUtils.isEmpty(values) || isTablesLessThanTwo()) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isTablesLessThanTwo() {
        return values.size() < MINIMUM_NUMBER_OF_GROUP_TABLE;
    }

    public List<Long> getOrderTableIds() {
        return values.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public boolean isDifferentSize(OrderTables orderTables) {
        return this.values.size() != orderTables.values.size();
    }

    public void validateGroupingTables() {
        for (final OrderTable orderTable : this.values) {
            validateGroupingTable(orderTable);
        }
    }

    private void validateGroupingTable(OrderTable orderTable) {
        if (orderTable.isNotEmpty() || orderTable.isGrouping()) {
            throw new IllegalArgumentException();
        }
    }

    public void groupingTables(Long tableGroupId) {
        for (OrderTable orderTable : this.values) {
            orderTable.grouping(tableGroupId);
            orderTable.changeEmpty(false);
        }
    }

    public void ungroupTables() {
        for (final OrderTable orderTable : values) {
            orderTable.ungroup();
            orderTable.changeEmpty(false);
        }
    }

    public List<OrderTable> getValues() {
        return values;
    }
}
