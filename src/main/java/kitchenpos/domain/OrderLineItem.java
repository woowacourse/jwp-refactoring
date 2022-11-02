package kitchenpos.domain;

public class OrderLineItem {

    private final Long id;
    private final Long orderId;
    private final Long orderedMenuId;
    private final long quantity;

    public OrderLineItem(final Long id, final Long orderId, final Long orderedMenuId, final long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.orderedMenuId = orderedMenuId;
        this.quantity = quantity;
    }

    public OrderLineItem(final Long orderId, final Long orderedMenuId, final long quantity) {
        this(null, orderId, orderedMenuId, quantity);
    }

    public OrderLineItem(final Long orderedMenuId, final long quantity) {
        this(null, null, orderedMenuId, quantity);
    }

    public OrderLineItem(final long quantity) {
        this(null, null, null, quantity);
    }

    public OrderLineItem addOreredMenuId(final Long orderedMenuId) {
        return new OrderLineItem(orderedMenuId, quantity);
    }

    public Long getId() {
        return id;
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
