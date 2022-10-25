package kitchenpos.domain.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemFixture() {
    }

    public static OrderLineItemFixture 주문_항목() {
        return new OrderLineItemFixture();
    }

    public OrderLineItemFixture 메뉴_아이디(final Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public OrderLineItem build() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
