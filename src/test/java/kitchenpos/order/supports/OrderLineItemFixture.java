package kitchenpos.order.supports;

import kitchenpos.menu.domain.model.Menu;
import kitchenpos.menu.supports.MenuFixture;
import kitchenpos.order.domain.model.OrderLineItem;

public class OrderLineItemFixture {

    private Long seq = null;
    private Menu menu = MenuFixture.fixture().build();
    private long quantity = 3L;

    private OrderLineItemFixture() {
    }

    public static OrderLineItemFixture fixture() {
        return new OrderLineItemFixture();
    }

    public OrderLineItemFixture seq(Long seq) {
        this.seq = seq;
        return this;
    }

    public OrderLineItemFixture menu(Menu menu) {
        this.menu = menu;
        return this;
    }

    public OrderLineItemFixture quantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderLineItem build() {
        return new OrderLineItem(seq, menu, quantity);
    }
}
