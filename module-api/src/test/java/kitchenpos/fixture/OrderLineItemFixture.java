package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    public static OrderLineItem 후라이드_두마리_주문() {
        Menu 메뉴_후라이드 = MenuFixture.메뉴_후라이드();
        Order 조리_상태의_주문 = OrderFixture.조리_상태의_주문();
        return new OrderLineItem(1L, 조리_상태의_주문.getId(), 메뉴_후라이드.getId(), 2L);
    }
}
