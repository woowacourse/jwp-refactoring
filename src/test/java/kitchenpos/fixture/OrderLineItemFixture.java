package kitchenpos.fixture;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 메뉴와_수량으로_주문_생성(Long orderId, Long menuId, long quantity) {
        return new OrderLineItem(orderId, menuId, quantity);
    }
}
