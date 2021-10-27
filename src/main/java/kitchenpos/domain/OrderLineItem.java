package kitchenpos.domain;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItem(Long menuId, long quantity) {
        this(null, null, menuId, quantity);
    }

    public OrderLineItem(Long orderId, Long menuId, long quantity) {
        this(null, orderId, menuId, quantity);
    }

    public OrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void addOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
