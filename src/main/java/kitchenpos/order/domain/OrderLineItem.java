package kitchenpos.order.domain;

import java.math.BigDecimal;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private String name;
    private BigDecimal price;
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long orderId, Long menuId, String name, BigDecimal price, long quantity) {
        this(null, orderId, menuId, name, price, quantity);
    }

    public OrderLineItem(Long seq, Long orderId, Long menuId, String name, BigDecimal price, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
