package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    public static OrderLineItem 주문항목_망고치킨_2개() {
        return new OrderLineItem(MenuFixture.메뉴_망고치킨_17000원_신메뉴(), 2L);
    }
}
