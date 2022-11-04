package kitchenpos.order.domain;

import kitchenpos.common.domain.Price;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private String name;
    private Price price;
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long seq, Long orderId, Long menuId, String name, Price price, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderLineItem(Long orderId, Long menuId, String name, Price price, long quantity) {
        this(null, orderId, menuId, name, price, quantity);
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

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
