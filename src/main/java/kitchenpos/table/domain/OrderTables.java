package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    private static final int MINIMUM_NUMBER_OF_GROUP_TABLE = 2;

    @OneToMany(mappedBy = "tableGroupId")
    private List<OrderTable> values;

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> values) {
        this.values = values;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(this.values);
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
        }
    }

    public void ungroupTables() {
        for (final OrderTable orderTable : values) {
            orderTable.ungroup();
        }
    }

    public List<OrderTable> getValues() {
        return values;
    }
}
