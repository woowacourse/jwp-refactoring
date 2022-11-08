package kitchenpos.order.domain;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long orderMenuId;
    private long quantity;

    public OrderLineItem(Long orderMenuId, long quantity) {
        this.orderMenuId = orderMenuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long seq, Long orderId, Long orderMenuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.orderMenuId = orderMenuId;
        this.quantity = quantity;
    }

    public void associateOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getOrderMenuId() {
        return orderMenuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
