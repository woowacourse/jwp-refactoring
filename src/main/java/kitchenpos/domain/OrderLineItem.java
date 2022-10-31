package kitchenpos.domain;

public class OrderLineItem {

    private final Long id;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public OrderLineItem(final Long id, final Long orderId, final Long menuId, final long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(final Long orderId, final Long menuId, final long quantity) {
        this(null, orderId, menuId, quantity);
    }

    public OrderLineItem(final Long menuId, final long quantity) {
        this(null, null, menuId, quantity);
    }

    public Long getId() {
        return id;
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
