package kitchenpos.dto;

import java.util.List;

public class OrderStatusValidateByIdsEvent {

    private final List<Long> orderTableIds;

    public OrderStatusValidateByIdsEvent(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
