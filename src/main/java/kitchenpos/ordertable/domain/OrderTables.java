package kitchenpos.ordertable.domain;

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
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }
    }

    public void validateIsEmptyAndTableGroupIdIsNull() {
        final boolean isEmptyAndTableGroupIdIsNull = values.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId()));

        if (isEmptyAndTableGroupIdIsNull) {
            throw new IllegalArgumentException("주문 테이블이 비어있지 않거나 이미 그룹화된 테이블입니다.");
        }
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
