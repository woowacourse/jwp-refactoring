package kitchenpos.order;

import java.math.BigDecimal;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;
    private String name;
    private BigDecimal price;

    public OrderLineItem(Long seq, Long orderId, Long menuId, long quantity, String name, BigDecimal price) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public static OrderLineItem of(Long menuId, long quantity) {
        return new OrderLineItem(null, null, menuId, quantity, null, null);
    }

    public static OrderLineItem of(Long menuId, long quantity, String name, BigDecimal price) {
        return new OrderLineItem(null, null, menuId, quantity, name, price);
    }

    public static OrderLineItem of(Long orderId, Long menuId, long quantity, String name, BigDecimal price) {
        return new OrderLineItem(null, orderId, menuId, quantity, name, price);
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

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
