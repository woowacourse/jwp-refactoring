package kitchenpos.table.domain;

import java.util.List;

public class TableUngroupedEvent {

    private final List<Long> orderTableIds;

    public TableUngroupedEvent(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
