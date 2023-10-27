package kitchenpos.order.domain;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long orderedMenuId;
    private long quantity;

    public OrderLineItem(Long seq, Long orderId, Long orderedMenuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.orderedMenuId = orderedMenuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getOrderedMenuId() {
        return orderedMenuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
