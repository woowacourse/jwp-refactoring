package kitchenpos.fixtures.domain;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem createOrderLineItem(final Long menuId,
                                                    final long quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static OrderLineItem createOrderLineItem(final Long orderId,
                                                    final Long menuId,
                                                    final long quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrder(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static class OrderLineItemRequestBuilder {

        private Long orderId;
        private Long menuId;
        private long quantity = 0;

        public OrderLineItemRequestBuilder orderId(final Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderLineItemRequestBuilder menuId(final Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public OrderLineItemRequestBuilder quantity(final Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setOrder(orderId);
            orderLineItem.setMenuId(menuId);
            orderLineItem.setQuantity(quantity);
            return orderLineItem;
        }
    }
}
