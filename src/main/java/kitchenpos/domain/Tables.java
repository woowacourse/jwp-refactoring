package kitchenpos.domain;

import java.util.List;
import java.util.Objects;

public class Tables {
    private final List<OrderTable> orderTables;

    public Tables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(List<OrderTable> orderTables){
        validateNoGroupedTable(orderTables);
        validateTableIsEmpty(orderTables);
    }

    private void validateTableIsEmpty(List<OrderTable> orderTables) {
        if (isAllEmpty(orderTables)) {
            throw new IllegalArgumentException("등록되는 모든 테이블들은 비어있어야 한다.");
        }
    }

    private boolean isAllEmpty(List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty());
    }

    private void validateNoGroupedTable(List<OrderTable> orderTables) {
        if (isAllNoGroupId(orderTables)) {
            throw new IllegalArgumentException("등록되는 모든 테이블들은 기존 단체 지정이 없어야 한다.");
        }
    }

    private boolean isAllNoGroupId(List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> Objects.nonNull(orderTable.getTableGroupId()));
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
