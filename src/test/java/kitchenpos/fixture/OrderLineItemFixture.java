package kitchenpos.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    public static OrderLineItem 후라이드_두마리_주문() {
        Menu 메뉴_후라이드 = MenuFixture.메뉴_후라이드();
        Order 조리_상태의_주문 = OrderFixture.조리_상태의_주문();
        return new OrderLineItem(1L, 조리_상태의_주문.getId(), 메뉴_후라이드.getId(), 2L);
    }
}
