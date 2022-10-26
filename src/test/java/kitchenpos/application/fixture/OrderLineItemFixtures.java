package kitchenpos.application.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixtures {

    public static final OrderLineItem generateOrderLineItem(final Long menuId, final long quantity) {
        return generateOrderLineItem(null, null, menuId, quantity);
    }

    public static final OrderLineItem generateOrderLineItem(final Long seq, final OrderLineItem orderLineItem) {
        return generateOrderLineItem(
                seq,
                orderLineItem.getOrderId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
        );
    }

    public static final OrderLineItem generateOrderLineItem(final Long seq,
                                                            final Long orderId,
                                                            final Long menuId,
                                                            final long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
