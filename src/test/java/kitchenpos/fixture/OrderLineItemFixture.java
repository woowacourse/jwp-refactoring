package kitchenpos.fixture;

import kitchenpos.order.domain.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    public static OrderLineItem 주문_품목_생성(final Long menuId,
                                         final Long quantity) {
        return new OrderLineItem(menuId, quantity);
    }
}
