package kitchenpos.supports;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    private Long seq = null;
    private Order order = OrderFixture.fixture().build();
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

    public OrderLineItemFixture order(Order order) {
        this.order = order;
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
        return new OrderLineItem(seq, order, menu, quantity);
    }
}
