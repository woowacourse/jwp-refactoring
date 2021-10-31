package kitchenpos.ordertable.application;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableStartedToChangeEmptyEvent {
    private Long orderTableId;

    public OrderTableStartedToChangeEmptyEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
