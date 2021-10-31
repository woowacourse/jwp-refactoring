package kitchenpos.fixtures;

import kitchenpos.domain.OrderLineItem;

import static kitchenpos.fixtures.MenuFixture.치즈폭탄;
import static kitchenpos.fixtures.ProductFixture.알리오갈리오;

public class OderLineItemFixture {

    public static OrderLineItem 첫번째주문의아이템1() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(치즈폭탄().getId());
        orderLineItem.setQuantity(2);
        return orderLineItem;
    }

    public static OrderLineItem 첫번째주문의아이템2() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(2L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(알리오갈리오().getId());
        orderLineItem.setQuantity(1);
        return orderLineItem;
    }


}
