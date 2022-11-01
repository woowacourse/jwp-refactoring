package kitchenpos.domain.order;

public class OrderLineItem {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    /**
     * DB 에 저장되지 않은 객체
     */
    public OrderLineItem(final Long menuId, final long quantity) {
        this(null, null, menuId, quantity);
    }

    /**
     * DB 애 저장하기 위한 객체
     */
    public OrderLineItem(final Long orderId, final Long menuId, final long quantity) {
        this(null, orderId, menuId, quantity);
    }

    /**
     * DB 에 저장된 객체
     */
    public OrderLineItem(final Long seq, final Long orderId, final Long menuId, final long quantity) {
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
}
