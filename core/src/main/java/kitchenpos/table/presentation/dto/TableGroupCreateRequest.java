package kitchenpos.table.presentation.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<Long> orderTables;

    public TableGroupCreateRequest(List<Long> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private static void validate(final List<Long> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("두개 이상의 테이블만 그룹화가 가능합니다.");
        }
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
