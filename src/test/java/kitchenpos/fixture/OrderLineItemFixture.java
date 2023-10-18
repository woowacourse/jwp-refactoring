package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    public static OrderLineItem 주문_품목_생성(final Menu menu,
                                         final Long quantity) {
        return new OrderLineItem(null, menu, quantity);
    }
}
