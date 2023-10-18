package kitchenpos.domain;

public class OrderLineItem2 {
    private Long seq;
    private Menu2 menu;
    private long quantity;

    public OrderLineItem2(
        final Long seq,
        final Menu2 menu,
        final long quantity
    ) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderLineItem2(final Menu2 menu, final long quantity) {
        this(null,menu, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Menu2 getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
