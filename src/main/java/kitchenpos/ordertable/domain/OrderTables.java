package kitchenpos.ordertable.domain;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.relational.core.mapping.MappedCollection;

public class OrderTables implements Iterable<OrderTable> {
    @MappedCollection(idColumn = "TABLE_GROUP_ID", keyColumn = "ID")
    private final List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public boolean hasSize(int expected) {
        return Objects.equals(expected, orderTables.size());
    }

    public boolean isPersistableInTableGroup() {
        if (orderTables.size() < 2) {
            return false;
        }

        if (orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId()))) {
            return false;
        }

        return true;
    }

    public void validateOrderTablesUnGroupable(List<OrderTableUngroupValidator> validators) {
        for (OrderTableUngroupValidator validator : validators) {
            validator.validate(this);
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<OrderTable> iterator() {
        return this.orderTables.iterator();
    }

    public void ungroup(final List<OrderTableUngroupValidator> orderTableUngroupValidators) {
        validateOrderTablesUnGroupable(orderTableUngroupValidators);
        for (OrderTable orderTable : orderTables) {
            orderTable.upgroup();
        }
    }
}
