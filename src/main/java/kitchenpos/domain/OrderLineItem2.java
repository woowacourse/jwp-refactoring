package kitchenpos.domain;

public class OrderLineItem2 {
    private Long seq;
    private Long menuId;
    private long quantity;

    public OrderLineItem2(
        final Long seq,
        final Long menuId,
        final long quantity
    ) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem2(final Long menuId, final long quantity) {
        this(null, menuId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
