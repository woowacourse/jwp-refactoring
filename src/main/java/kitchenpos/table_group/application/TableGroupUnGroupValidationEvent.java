package kitchenpos.table_group.application;

import java.util.List;

public class TableGroupUnGroupValidationEvent {

    private final List<Long> orderTableIds;

    public TableGroupUnGroupValidationEvent(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
