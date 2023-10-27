package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    private static final long DEFAULT_QUANTITY = 1L;

    public static OrderLineItem 주문_상품_생성(final Menu menu) {
        return new OrderLineItem(menu, DEFAULT_QUANTITY);
    }

    public static OrderLineItems 주문_상품들_생성(final List<Menu> menus) {
        final List<OrderLineItem> 주문_상품들 = new ArrayList<>();

        for (Menu menu : menus) {
            주문_상품들.add(주문_상품_생성(menu));
        }

        return new OrderLineItems(주문_상품들);
    }
}
