package kitchenpos.table.domain;

public class OrderTableChangeEmptyEvent {

    private final Long orderTableId;
    private final boolean empty;

    public OrderTableChangeEmptyEvent(Long orderTableId, boolean empty) {
        this.orderTableId = orderTableId;
        this.empty = empty;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public boolean isEmpty() {
        return empty;
    }
}
