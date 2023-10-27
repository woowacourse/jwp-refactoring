package kitchenpos.table.event;

import java.util.List;

public class CheckOrderProceedingEvent {

    private final List<Long> orderTableIds;

    public CheckOrderProceedingEvent(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
