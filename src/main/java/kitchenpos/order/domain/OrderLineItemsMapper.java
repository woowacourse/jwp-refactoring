package kitchenpos.order.domain;

import org.springframework.stereotype.Component;

@Component
public class OrderLineItemsMapper {

    public OrderLineItem mapItem(OrderLineItem entity, Long orderId, Long menuId) {
        return new OrderLineItem(entity.getSeq(), orderId, menuId, entity.getQuantity());
    }
}
