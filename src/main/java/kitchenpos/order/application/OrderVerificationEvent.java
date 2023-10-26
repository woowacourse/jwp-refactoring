package kitchenpos.order.application;

public class OrderVerificationEvent {

    private final Object source;
    private final Long orderTableId;

    public OrderVerificationEvent(Object source,
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
