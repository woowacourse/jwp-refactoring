package kitchenpos.common.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {


    public static OrderLineItem 주문_항목(Long menuId) {
        return new OrderLineItem(menuId, 1L);
    }

    public static OrderLineItem 주문_항목(Long menuId, Long orderId) {
        return new OrderLineItem(menuId, orderId, 1L);
    }

    public static OrderLineItem 주문_항목(Long orderLineItemId, Long menuId, Long orderId) {
        return new OrderLineItem(orderLineItemId, menuId, orderId, 1L);
    }
}
