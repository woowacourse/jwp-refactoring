package kitchenpos.domain;

import java.util.List;
import java.util.Objects;

public class Tables {
    private List<OrderTable> orderTables;

    public Tables(List<OrderTable> orderTables) {
        validateNoGroupedTable(orderTables);
        validateTableIsEmpty(orderTables);
        this.orderTables = orderTables;
    }

    private void validateTableIsEmpty(List<OrderTable> orderTables) {
        if (orderTables.stream().anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new IllegalArgumentException("등록되는 모든 테이블들은 비어있어야 한다.");
        }
    }

    private void validateNoGroupedTable(List<OrderTable> orderTables) {
        if (orderTables.stream().anyMatch(orderTable -> Objects.nonNull(orderTable.getTableGroupId()))) {
            throw new IllegalArgumentException("등록되는 모든 테이블들은 기존 단체 지정이 없어야 한다.");
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
