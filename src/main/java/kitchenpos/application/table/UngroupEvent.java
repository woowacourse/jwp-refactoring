package kitchenpos.application.table;

import java.util.List;

public class UngroupEvent {

    private final List<Long> orderTableIds;

    public UngroupEvent(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
