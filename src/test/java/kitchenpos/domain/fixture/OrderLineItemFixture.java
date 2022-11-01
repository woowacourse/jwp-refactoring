package kitchenpos.domain.fixture;

import kitchenpos.domain.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemFixture() {
    }

    public static OrderLineItem 주문_항목_1번(final Long menuId) {
        return 주문_항목()
            .메뉴_아이디(menuId)
            .build();
    }

    private static OrderLineItemFixture 주문_항목() {
        return new OrderLineItemFixture();
    }

    private OrderLineItemFixture 메뉴_아이디(final Long menuId) {
        this.menuId = menuId;
        return this;
    }

    private OrderLineItem build() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
