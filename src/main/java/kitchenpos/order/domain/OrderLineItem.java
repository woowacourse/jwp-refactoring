package kitchenpos.order.domain;

public class OrderLineItem {

    private final Long id;
    private final Long orderId;
    private final OrderMenu orderMenu;
    private final long quantity;

    public OrderLineItem(Long id, Long orderId, OrderMenu orderMenu, long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.orderMenu = orderMenu;
        this.quantity = quantity;
    }

    public OrderLineItem(Long orderId, OrderMenu orderMenu, long quantity) {
        this(null, orderId, orderMenu, quantity);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return orderMenu.getMenuId();
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public long getQuantity() {
        return quantity;
    }
}
