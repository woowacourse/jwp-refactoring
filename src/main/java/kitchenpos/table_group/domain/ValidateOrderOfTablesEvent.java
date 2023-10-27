package kitchenpos.table_group.domain;

import java.util.List;

public class ValidateOrderOfTablesEvent {
    private final List<Long> orderTableIds;

    public ValidateOrderOfTablesEvent(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
