package kitchenpos.order.dto;

import java.util.List;

public class ValidateSameSizeOrderTableEvent {

    private final List<Long> orderTableIds;

    public ValidateSameSizeOrderTableEvent(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
