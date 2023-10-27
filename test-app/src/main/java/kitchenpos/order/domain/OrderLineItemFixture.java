package kitchenpos.order.domain;

import kitchenpos.order.vo.MenuSpecification;
import kitchenpos.order.vo.Quantity;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    public static OrderLineItem 주문_항목(Long menuId, MenuSpecification menuSpecification) {
        return new OrderLineItem(menuId, Quantity.valueOf(1L), menuSpecification);
    }

    public static OrderLineItem 주문_항목(Long menuId, Long orderId, MenuSpecification menuSpecification) {
        return new OrderLineItem(menuId, orderId, Quantity.valueOf(1L), menuSpecification);
    }
}
