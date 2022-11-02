package kitchenpos.event;

import java.util.List;

public class CheckTableCanChangeEvent {

    private final List<Long> orderTableIds;

    public CheckTableCanChangeEvent(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
