package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    public static OrderLineItem 메뉴을_가진_주문_항목_생성(Menu savedMenu, long quantity) {
        return new OrderLineItem(
                savedMenu.getId(),
                quantity
        );
    }

    public static OrderLineItem 존재하지_않는_메뉴를_가진_주문_항목_생성() {
        return new OrderLineItem(
                Long.MAX_VALUE,
                1
        );
    }

}
