package kitchenpos.common.event;

import java.util.List;

public class ValidateAppendOrderTableInTableGroupEvent {

    private final List<Long> orderTableIds;

    public ValidateAppendOrderTableInTableGroupEvent(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
