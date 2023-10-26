package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuSnapShot;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 주문상품(Menu menu, long quantity) {
        return new OrderLineItem(menu.getId(), MenuSnapShot.make(menu), quantity);
    }
}
