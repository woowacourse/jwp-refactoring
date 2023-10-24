package kitchenpos.domain.order;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long menuId, long quantity) {
        return new OrderLineItem(null,null, menuId, quantity);
    }

    public static OrderLineItem of(Long orderId, Long menuId, long quantity) {
        return new OrderLineItem(null, orderId, menuId, quantity);
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
