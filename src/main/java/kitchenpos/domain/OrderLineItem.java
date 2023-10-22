package kitchenpos.domain;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private Quantity quantity;

    public OrderLineItem(Long seq, Long orderId, Long menuId, Quantity quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long menuId, Quantity quantity) {
        this(null, null, menuId, quantity);
    }

    public OrderLineItem(Long orderId, Long menuId, Quantity quantity) {
        this(null, orderId, menuId, quantity);
    }

    public OrderLineItem() {
    }

    public static OrderLineItem of(Long menuId, long quantity) {
        return new OrderLineItem(menuId, Quantity.valueOf(quantity));
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

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public long getQuantityValue() {
        return quantity.getValue();
    }
}
