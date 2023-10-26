package kitchenpos.table.application;

public class OrderTableChangeEmptyEvent {

    private final Object source;
    private final Long orderTableId;

    public OrderTableChangeEmptyEvent(Object source,
                                 Long orderTableId) {
        this.source = source;
        this.orderTableId = orderTableId;
    }

    public Object getSource() {
        return source;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
