package kitchenpos.common;

import java.util.List;

public class ValidateOrderTablesOrderStatusEvent {

    private final List<Long> orderTableIds;

    public ValidateOrderTablesOrderStatusEvent(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
