package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
    public static final OrderLineItem FIRST_FIRST_ORDERLINE;
    public static final OrderLineItem FIRST_FIRST_ORDERLINE_NO_SEQ_NO_ORDER;

    static {
        FIRST_FIRST_ORDERLINE = newInstance(1L, 1L, 1L, 1);
        FIRST_FIRST_ORDERLINE_NO_SEQ_NO_ORDER = newInstance(null, null, 1L, 1);
    }

    private static OrderLineItem newInstance(Long seq, Long orderId, Long menuId, int quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
