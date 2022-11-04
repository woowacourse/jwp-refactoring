package kitchenpos.order.domain;

public class OrderLineItem {
    private final Long seq;
    private Long orderId;
    private final long quantity;

    public OrderLineItem(final Long seq, final Long orderId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public long getQuantity() {
        return quantity;
    }
}
