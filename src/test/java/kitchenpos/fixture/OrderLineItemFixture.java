package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    public static OrderLineItem 주문_품목_생성(final Long orderId,
                                         final Long menuId,
                                         int quantity) {
        return new OrderLineItem(null, orderId, menuId, quantity);
    }
}
