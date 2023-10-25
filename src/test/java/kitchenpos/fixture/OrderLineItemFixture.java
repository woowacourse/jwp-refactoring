package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuSnapShot;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 주문상품(Menu menu, long quantity) {
        return new OrderLineItem(MenuSnapShot.make(menu), quantity);
    }
}
