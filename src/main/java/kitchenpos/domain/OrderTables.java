package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderTables {

    private final List<OrderTable> values;

    public OrderTables(List<OrderTable> values) {
        this.values = values;
    }

    public void validateSize(int size) {
        if (values.size() != size) {
            throw new IllegalArgumentException();
        }
    }

    public void validateIsEmptyAndTableGroupIdIsNull() {
        values.stream()
                .filter(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId()))
                .anyMatch(orderTable -> {
                    throw new IllegalArgumentException();
                });
    }

    public void updateTableGroupIdAndEmpty(final Long tableGroupId, final boolean empty) {
        for (final OrderTable savedOrderTable : values) {
            savedOrderTable.updateTableGroupId(tableGroupId);
            savedOrderTable.updateEmpty(empty);
        }
    }

    public List<Long> mapOrderTableIds() {
        return values.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
