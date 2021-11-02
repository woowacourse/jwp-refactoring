package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {


    public OrderLineItem 주문_메뉴_생성(Long orderId, Long menuId, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public OrderLineItem 주문_메뉴_생성(Long seq, Long orderId, Long menuId, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public List<OrderLineItem> 주문_메뉴_리스트_생성(OrderLineItem... orderLineItems) {
        return Arrays.asList(orderLineItems);
    }
}
