package kitchenpos.event;

import java.util.List;

public class VerifiedAbleToUngroupEvent {

    private final List<Long> orderTableIds;

    public VerifiedAbleToUngroupEvent(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

}
