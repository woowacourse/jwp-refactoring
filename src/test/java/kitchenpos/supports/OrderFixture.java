package kitchenpos.supports;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixture {

    private static final long DEFAULT_QUANTITY = 2;

    public static Order of(final Long menuId, final Long orderTableId){
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(DEFAULT_QUANTITY);

        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(List.of(orderLineItem));

        return order;
    }

    public static Order createMeal(){
        final Order order = new Order();
        order.setOrderStatus("MEAL");
        return order;
    }

    public static Order createCompletion(){
        final Order order = new Order();
        order.setOrderStatus("COMPLETION");
        return order;
    }
}
