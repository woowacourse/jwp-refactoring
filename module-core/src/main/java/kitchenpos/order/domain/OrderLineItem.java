package kitchenpos.order.domain;

import java.math.BigDecimal;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private Long quantity;
    private String name;
    private BigDecimal price;

    public OrderLineItem(Long seq, Long orderId, Long menuId, Long quantity) {
        this(seq, orderId, menuId, quantity, null, null);
    }

    public OrderLineItem(Long orderId, Long menuId, Long quantity, String name, BigDecimal price) {
        this(null, orderId, menuId, quantity, name, price);
    }

    public OrderLineItem(Long seq, Long orderId, Long menuId, Long quantity, String name,
            BigDecimal price) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
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

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
