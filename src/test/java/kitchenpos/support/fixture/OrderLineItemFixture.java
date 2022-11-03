package kitchenpos.support.fixture;

import kitchenpos.menu.domain.UpdatableMenuInfo;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.domain.Price;

public class OrderLineItemFixture {

    public static OrderLineItem create(final String name, final Long price) {
        final UpdatableMenuInfo updatableMenuInfo = new UpdatableMenuInfo(new Price(price), name);
        return new OrderLineItem(updatableMenuInfo, 0);
    }

    public static OrderLineItem create(final String name, final Price price) {
        final UpdatableMenuInfo updatableMenuInfo = new UpdatableMenuInfo(price, name);
        return new OrderLineItem(updatableMenuInfo, 0);
    }
}
