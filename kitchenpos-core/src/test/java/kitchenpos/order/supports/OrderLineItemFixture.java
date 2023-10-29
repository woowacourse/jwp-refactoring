package kitchenpos.order.supports;

import java.math.BigDecimal;
import kitchenpos.global.Price;
import kitchenpos.order.domain.model.MenuSnapShot;
import kitchenpos.order.domain.model.OrderLineItem;

public class OrderLineItemFixture {

    private Long seq = null;
    private Long menuId = 1L;
    private long quantity = 3L;
    private MenuSnapShot menuSnapShot = new MenuSnapShot("피자", new Price(new BigDecimal(10_000)));

    private OrderLineItemFixture() {
    }

    public static OrderLineItemFixture fixture() {
        return new OrderLineItemFixture();
    }

    public OrderLineItemFixture seq(Long seq) {
        this.seq = seq;
        return this;
    }

    public OrderLineItemFixture menuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public OrderLineItemFixture quantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderLineItem build() {
        return new OrderLineItem(seq, menuId, quantity, menuSnapShot);
    }
}
