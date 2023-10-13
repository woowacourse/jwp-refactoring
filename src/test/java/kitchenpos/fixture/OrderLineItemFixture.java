package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public final class OrderLineItemFixture {

    public static OrderLineItem 주문_항목_생성(final Long menuId) {
        final OrderLineItem orderLineItem = new OrderLineItem();

        orderLineItem.setMenuId(menuId);

        return orderLineItem;
    }

    private OrderLineItemFixture() {
    }
}
