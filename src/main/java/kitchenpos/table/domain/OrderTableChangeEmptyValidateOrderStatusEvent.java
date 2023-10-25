package kitchenpos.table.domain;

public final class OrderTableChangeEmptyValidateOrderStatusEvent {

    private final Long orderTableId;

    public OrderTableChangeEmptyValidateOrderStatusEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
