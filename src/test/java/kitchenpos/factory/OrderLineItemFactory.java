package kitchenpos.factory;

import org.springframework.stereotype.Component;

import kitchenpos.domain.OrderLineItem;

@Component
public class OrderLineItemFactory {
    public OrderLineItem create(Long seq, Long orderId, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public OrderLineItem create(Long menuId, long quantity) {
        return create(null, null, menuId, quantity);
    }
}
