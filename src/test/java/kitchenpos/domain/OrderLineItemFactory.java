package kitchenpos.domain;

public final class OrderLineItemFactory {

    private OrderLineItemFactory() {
    }

    public static OrderLineItem createOrderLineItemOf(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
