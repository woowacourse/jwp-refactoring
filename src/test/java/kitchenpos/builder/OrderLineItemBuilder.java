package kitchenpos.builder;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemBuilder {

    private Long seq;
    private Order order;
    private Long menuId;
    private long quantity;

    public OrderLineItemBuilder seq(Long seq) {
        this.seq = seq;
        return this;
    }

    public OrderLineItemBuilder order(Order order) {
        this.order = order;
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
        return new OrderLineItem(
                this.seq,
                this.order,
                this.menuId,
                this.quantity
        );
    }
}
