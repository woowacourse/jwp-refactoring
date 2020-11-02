package kitchenpos.builder;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemBuilder {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemBuilder() {
    }

    public OrderLineItemBuilder(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemBuilder seq(Long seq) {
        this.seq = seq;
        return this;
    }

    public OrderLineItemBuilder orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderLineItemBuilder menuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public OrderLineItemBuilder quantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderLineItem build() {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }
}
