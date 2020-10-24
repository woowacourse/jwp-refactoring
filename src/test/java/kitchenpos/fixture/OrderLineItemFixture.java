package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static final OrderLineItem ORDER_LINE_ITEM_FIXTURE_1 = new OrderLineItem();
    public static final OrderLineItem ORDER_LINE_ITEM_FIXTURE_2 = new OrderLineItem();

    static {
        ORDER_LINE_ITEM_FIXTURE_1.setMenuId(1L);
        ORDER_LINE_ITEM_FIXTURE_1.setOrderId(1L);
        ORDER_LINE_ITEM_FIXTURE_1.setQuantity(10);
        ORDER_LINE_ITEM_FIXTURE_2.setMenuId(2L);
        ORDER_LINE_ITEM_FIXTURE_2.setOrderId(2L);
        ORDER_LINE_ITEM_FIXTURE_2.setQuantity(5);
    }
}
