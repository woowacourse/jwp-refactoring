package kitchenpos.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    public static OrderLineItem 주문항목_망고치킨_2개() {
        final Menu menu = MenuFixture.메뉴_망고치킨_17000원_신메뉴();
        return new OrderLineItem(menu.getId(), 2L, menu.getName(), menu.getPrice());
    }

    public static OrderLineItem 주문항목_망고치킨_2개(final Menu menu) {
        return new OrderLineItem(menu.getId(), 2L, menu.getName(), menu.getPrice());
    }
}
