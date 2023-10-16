package kitchenpos.domain;

public final class OrderLineItemFactory {

    private OrderLineItemFactory() {
    }

    public static OrderLineItem createOrderLineItemOf(final Long menuId, final long quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
