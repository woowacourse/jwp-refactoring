package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    public static OrderLineItem 주문_아이템_엔티티_A = createOrderLineItem(1L, 10);

    private static OrderLineItem createOrderLineItem(Long seq, long quantity) {
        Menu 메뉴_엔티티_A = MenuFixture.메뉴_엔티티_A;

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setMenuId(메뉴_엔티티_A.getId());
        orderLineItem.setOrderId(1L);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
