package kitchenpos.order.domain;

import kitchenpos.menu.domain.Price;

public class OrderLineItem {

    private final Long seq;
    private final Long menuId;
    private final String name;
    private final Price price;

    private final long quantity;


    public OrderLineItem(final Long seq, final Long menuId, final String name, final Price price, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderLineItem(final Long menuId, final long quantity) {
        this(null, menuId, null, null, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
